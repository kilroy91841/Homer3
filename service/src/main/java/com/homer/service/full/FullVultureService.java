package com.homer.service.full;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.exception.ExistingVultureInProgressException;
import com.homer.exception.IllegalVultureDropPlayerException;
import com.homer.exception.NotVulturableException;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.service.ITeamService;
import com.homer.service.IVultureService;
import com.homer.service.auth.IUserService;
import com.homer.service.auth.User;
import com.homer.service.schedule.IScheduler;
import com.homer.type.PlayerSeason;
import com.homer.type.Vulture;
import com.homer.type.EventStatus;
import com.homer.util.EnvironmentUtility;
import com.homer.util.core.$;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by arigolub on 5/5/16.
 */
public class FullVultureService implements IFullVultureService {

    final static Logger logger = LoggerFactory.getLogger(FullVultureService.class);

    private static final String COMMISSIONER_EMAIL = "arigolub@gmail.com";

    private ITeamService teamService;
    private IPlayerService playerService;
    private IVultureService vultureService;
    private IPlayerSeasonService playerSeasonService;
    private IUserService userService;
    private IEmailService emailService;
    private IScheduler scheduler;

    public FullVultureService(IVultureService vultureService, IPlayerSeasonService playerSeasonService,
                              ITeamService teamService, IPlayerService playerService,
                              IUserService userService, IEmailService emailService,
                              IScheduler scheduler) {
        this.vultureService = vultureService;
        this.playerSeasonService = playerSeasonService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.userService = userService;
        this.emailService = emailService;
        this.scheduler = scheduler;
    }

    @Override
    public Vulture createVulture(long vultureTeamId, long playerId, @Nullable Long dropPlayerId, boolean isCommissionerVulture) {
        //Ensure player is vulturable
        PlayerSeason playerSeason = playerSeasonService.getCurrentPlayerSeason(playerId);
        if (playerSeason == null) {
            logErrorAndThrow("Could not find player season for " + playerId);
        }
        if (!playerSeason.getVulturable()) {
            String message = "Player " + playerId + " cannot be vultured";
            logger.error(message);
            throw new NotVulturableException(message);
        }

        //Check for existing vulture
        Vulture existingVulture = vultureService.getForPlayer(playerId, true);
        if (existingVulture != null) {
            String message = "An in progress vulture already exists for " + playerId;
            logger.error(message);
            throw new ExistingVultureInProgressException(message);
        }

        //All good, create vulture
        Vulture vulture = new Vulture();
        vulture.setPlayerId(playerId);
        vulture.setDropPlayerId(dropPlayerId);
        vulture.setTeamId(vultureTeamId);
        vulture.setDeadlineUTC(
                DateTime.now(DateTimeZone.UTC).plusMinutes(
                        EnvironmentUtility.getInstance().getVultureExpirationMinutes()));
        vulture.setVultureStatus(EventStatus.IN_PROGRESS);
        vulture.setIsCommisionerVulture(isCommissionerVulture);

        logger.info("Create vulture: " + vulture);

        Vulture createdVulture = vultureService.upsert(vulture);

        scheduleVulture(createdVulture);

        sendVultureEmail(createdVulture, playerSeason);

        return createdVulture;
    }

    @Override
    public Vulture resolveVulture(long id) {
        Vulture vulture = vultureService.getById(id);
        if (vulture == null) {
            scheduler.cancel(Vulture.class, id);
            throw new IllegalArgumentException("Could not find vulture for id " + id);
        }

        long playerId = vulture.getPlayerId();
        PlayerSeason playerSeason = playerSeasonService.getCurrentPlayerSeason(playerId);
        if (playerSeason == null) {
            scheduler.cancel(Vulture.class, id);
            markVultureAsErrorAndThrow(vulture, "Could not find player season for " + playerId);
        }

        if (playerSeason.getVulturable()) {
            playerSeason.setVulturable(false);
            try {
                sendVultureSuccessfulEmail(vulture, playerSeason);
                movePlayersForSuccessfulVulture(vulture, playerSeason);
                vulture.setVultureStatus(EventStatus.SUCCESSFUL);
            } catch (IllegalVultureDropPlayerException e) {
                vulture.setVultureStatus(EventStatus.INVALID);
            }
        } else {
            vulture.setVultureStatus(EventStatus.FIXED);

            sendVultureFailureEmail(vulture, playerSeason);
        }

        scheduler.cancel(Vulture.class, id);
        return vultureService.upsert(vulture);
    }

    @Override
    public boolean markInProgressVultureForPlayerAsFixed(long playerId) {
        Vulture existingVulture = vultureService.getForPlayer(playerId, true);
        if (existingVulture == null) {
            return true;
        }

        existingVulture.setVultureStatus(EventStatus.FIXED);
        vultureService.upsert(existingVulture);

        boolean cancelled = scheduler.cancel(Vulture.class, existingVulture.getId());
        if (cancelled) {
            logger.info("Vulture " + existingVulture.getId() + " is cancelled, removing from map");
        } else {
            logger.info("Vulture " + existingVulture.getId() + " attempt to cancel failed");
        }

        logger.info("Attempted to cancel vulture " + existingVulture.getId() + ", successful? " + cancelled);

        return cancelled;
    }

    @Override
    public List<Vulture> getInProgressVultures() {
        List<Vulture> inProgressVultures = vultureService.getInProgressVultures();
        for(Vulture vulture : inProgressVultures) {
            hydrateVulture(vulture);
        }
        return inProgressVultures;
    }

    @Override
    public List<PlayerSeason> getVulturablePlayerSeasons() {
        List<PlayerSeason> vulturablePlayerSeasons = playerSeasonService.getVulturablePlayerSeasons();
        Map<Long, Vulture> inProgressVultures = $.of(getInProgressVultures()).toMap(Vulture::getPlayerId);
        return $.of(vulturablePlayerSeasons).filterToList(ps -> !inProgressVultures.containsKey(ps.getPlayerId()));
    }

    private void movePlayersForSuccessfulVulture(Vulture vulture, PlayerSeason playerSeason) {
        long teamId = vulture.getTeamId();
        playerSeason = playerSeasonService.switchTeam(playerSeason, playerSeason.getTeamId(), teamId);

        Long dropPlayerId = vulture.getDropPlayerId();
        PlayerSeason dropPlayerSeason = null;
        if (dropPlayerId != null) {
            dropPlayerSeason = playerSeasonService.getCurrentPlayerSeason(dropPlayerId);
            if (dropPlayerSeason == null) {
                markVultureAsErrorAndThrow(vulture, "Could not find player season for " + dropPlayerId);
            }
            try {
                dropPlayerSeason = playerSeasonService.switchTeam(dropPlayerSeason, teamId, null);
            } catch (IllegalArgumentException e) {
                throw new IllegalVultureDropPlayerException(e.getMessage());
            }
        }

        playerSeasonService.upsert(playerSeason);
        if (dropPlayerSeason != null) {
            playerSeasonService.upsert(dropPlayerSeason);
        }
    }

    private void markVultureAsErrorAndThrow(Vulture vulture, String message) {
        vulture.setVultureStatus(EventStatus.ERROR);
        vultureService.upsert(vulture);
        logErrorAndThrow(message);
    }

    private void hydrateVulture(Vulture vulture) {
        vulture.setPlayer(playerService.getById(vulture.getPlayerId()));
        vulture.setDropPlayer(playerService.getById(vulture.getDropPlayerId()));
        vulture.setVultureTeam(teamService.getById(vulture.getTeamId()));
    }

    @Override
    public void scheduleVulture(Vulture createdVulture) {
        Runnable runnable = () -> {
            try {
                resolveVulture(createdVulture.getId());
            } catch (Exception e) {
                logger.error("Error resolving vulture: " + e.getMessage());
            }
        };
        scheduler.schedule(createdVulture, runnable);
    }

    private void sendVultureEmail(Vulture vulture, PlayerSeason playerSeason) {
        List<String> emails = getVultureEmails(vulture, playerSeason);

        hydrateVulture(vulture);

        String title = vulture.getPlayer().getName() + " has been vultured";

        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(HtmlObject.of(HtmlTag.DIV).body(vulture.getPlayer().getName() + " has been vultured by " + vulture.getVultureTeam().getName() + ". "))
                .child(HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/vulture").body("Click here to see it in the app"));

        EmailRequest emailRequest = new EmailRequest(emails, title, htmlObject);
        emailService.sendEmail(emailRequest);
    }

    private void sendVultureSuccessfulEmail(Vulture vulture, PlayerSeason playerSeason) {
        List<String> emails = getVultureEmails(vulture, playerSeason);

        hydrateVulture(vulture);

        String title = "Vulture on " + vulture.getPlayer().getName() + " was successful!";

        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(HtmlObject.of(HtmlTag.DIV).body("The vulture on " + vulture.getPlayer().getName() + " was successful. " +
                        vulture.getVultureTeam().getName() + " can now add that player on ESPN. They also need to drop " +
                        vulture.getDropPlayer().getName() + "."))
                .child(HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/vulture").body("Click here to see it in the app"));

        EmailRequest emailRequest = new EmailRequest(emails, title, htmlObject);
        emailService.sendEmail(emailRequest);
    }

    private void sendVultureFailureEmail(Vulture vulture, PlayerSeason playerSeason) {
        List<String> emails = getVultureEmails(vulture, playerSeason);

        hydrateVulture(vulture);

        String title = "Vulture on " + vulture.getPlayer().getName() + " has failed";

        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(HtmlObject.of(HtmlTag.DIV).body("The vulture on " + vulture.getPlayer().getName() + " was not successful."))
                .child(HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/vulture").body("Click here to see it in the app"));

        EmailRequest emailRequest = new EmailRequest(emails, title, htmlObject);
        emailService.sendEmail(emailRequest);
    }

    private List<String> getVultureEmails(Vulture vulture, PlayerSeason playerSeason) {
        List<User> usersToEmail = Lists.newArrayList();
        usersToEmail.addAll(userService.getUsersForTeam(vulture.getTeamId()));
        usersToEmail.addAll(userService.getUsersForTeam(playerSeason.getTeamId()));
        List<String> emails = $.of(usersToEmail).toList(User::getEmail);
        emails.add(COMMISSIONER_EMAIL);
        return emails;
    }

    private static void logErrorAndThrow(String message) {
        logger.error(message);
        throw new IllegalArgumentException(message);
    }
}
