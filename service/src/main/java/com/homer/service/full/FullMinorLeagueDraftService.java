package com.homer.service.full;

import com.google.common.collect.Lists;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.exception.IllegalMinorLeagueDraftPickException;
import com.homer.external.common.IMLBClient;
import com.homer.external.common.mlb.MLBPlayer;
import com.homer.service.IMinorLeaguePickService;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.service.ITeamService;
import com.homer.service.auth.IUserService;
import com.homer.service.auth.User;
import com.homer.service.gather.IGatherer;
import com.homer.service.schedule.IScheduler;
import com.homer.type.MinorLeaguePick;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.view.*;
import com.homer.util.EnumUtil;
import com.homer.util.EnvironmentUtility;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 6/10/16.
 */
public class FullMinorLeagueDraftService implements IFullMinorLeagueDraftService {

    final static Logger logger = LoggerFactory.getLogger(FullMinorLeagueDraftService.class);

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEEE MM/dd hh:mm a ZZZZ").withZoneUTC();

    private static final int MAX_MINOR_LEAGUERS = EnvironmentUtility.getInstance().getMaxMinorLeagueRosterSize();

    private IGatherer gatherer;
    private IMinorLeaguePickService minorLeaguePickService;
    private IFullPlayerService fullPlayerService;
    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;
    private IMLBClient mlbClient;
    private IEmailService emailService;
    private IUserService userService;
    private IScheduler scheduler;
    private ITeamService teamService;

    public FullMinorLeagueDraftService(IGatherer gatherer, IMinorLeaguePickService minorLeaguePickService,
                                       IPlayerService playerService, IPlayerSeasonService playerSeasonService,
                                       IFullPlayerService fullPlayerService, IMLBClient mlbClient,
                                       IEmailService emailService, IUserService userService,
                                       ITeamService teamService, IScheduler scheduler) {
        this.gatherer = gatherer;
        this.minorLeaguePickService = minorLeaguePickService;
        this.playerService = playerService;
        this.playerSeasonService = playerSeasonService;
        this.fullPlayerService = fullPlayerService;
        this.mlbClient = mlbClient;
        this.emailService = emailService;
        this.userService = userService;
        this.scheduler = scheduler;
        this.teamService = teamService;
    }

    @Override
    public MinorLeagueDraftView getMinorLeagueDraft(@Nullable Integer season) {
        MinorLeagueDraftView minorLeagueDraftView = new MinorLeagueDraftView();

        List<MinorLeaguePick> minorLeaguePicks = minorLeaguePickService.getMinorLeaguePicksBySeason(season != null ? season : LeagueUtil.SEASON);
        List<Long> playerIds = $.of(minorLeaguePicks).filter(mlp -> mlp.getPlayerId() != null).toList(MinorLeaguePick::getPlayerId);
        Map<Long, PlayerView> playerViews = $.of(gatherer.gatherPlayersByIds(playerIds)).toIdMap();

        List<MinorLeaguePickView> minorLeaguePickViews = Lists.newArrayList();
        for (MinorLeaguePick pick : minorLeaguePicks) {
            MinorLeaguePickView view = MinorLeaguePickView.from(pick);
            view.setPlayerView(playerViews.get(view.getPlayerId()));
            view.setOwningTeam(teamService.getFantasyTeamMap().get(pick.getOwningTeamId()));
            view.setOriginalTeam(teamService.getFantasyTeamMap().get(pick.getOriginalTeamId()));
            view.setSwapTeam(teamService.getFantasyTeamMap().get(pick.getSwapTeamId()));
            minorLeaguePickViews.add(view);
        }

        minorLeagueDraftView.setPicks(minorLeaguePickViews);
        minorLeagueDraftView.setCurrentPick(findCurrentPick(minorLeaguePickViews));

        return minorLeagueDraftView;
    }

    @Nullable
    private static MinorLeaguePickView findCurrentPick(List<MinorLeaguePickView> minorLeaguePickViews) {
        for (MinorLeaguePickView pick : minorLeaguePickViews) {
            if (pick.getPlayerId() == null && pick.getIsSkipped() == null) {
                return pick;
            }
        }
        return null;
    }

    @Override
    public MinorLeaguePickView draftPlayer(MinorLeaguePickView minorLeaguePick) throws IllegalMinorLeagueDraftPickException {
        Player player = minorLeaguePick.getPlayerView();
        if (minorLeaguePick.getPlayerId() != null || (minorLeaguePick.getIsSkipped() != null && !minorLeaguePick.getIsSkipped())) {
            throw new IllegalArgumentException("This pick has already been made");
        }

        List<PlayerSeason> currentMinorLeaguers = playerSeasonService.getMinorLeaguers(minorLeaguePick.getOwningTeamId(), LeagueUtil.SEASON);
        if (currentMinorLeaguers.size() >= MAX_MINOR_LEAGUERS) {
            throw new IllegalArgumentException(String.format("Max minor league roster size of %s already reached", MAX_MINOR_LEAGUERS));
        }

        Player existingPlayer;
        if (player.getMlbPlayerId() != null) {
            existingPlayer = playerService.getPlayerByMLBPlayerId(player.getMlbPlayerId());
            if (existingPlayer == null) {
                MLBPlayer mlbPlayer = mlbClient.getPlayer(player.getMlbPlayerId());
                if (mlbPlayer != null) {
                    player.setFirstName(mlbPlayer.getFirstName());
                    player.setLastName(mlbPlayer.getLastName());
                    player.setName(mlbPlayer.getName());
                    player.setPosition(EnumUtil.from(Position.class, mlbPlayer.getPositionId()));
                    player.setMlbTeamId(mlbPlayer.getTeamId());
                } else {
                    throw new IllegalMinorLeagueDraftPickException(String.format("No player with MLB playerId %s was found", player.getMlbPlayerId()));
                }
            }
        } else if (player.getName() != null) {
            existingPlayer = playerService.getPlayerByName(player.getName());
        } else {
            throw new IllegalMinorLeagueDraftPickException("Players can only be drafted via mlbPlayerId or name");
        }
        if (existingPlayer == null) {
            existingPlayer = fullPlayerService.createPlayer(player, true);
        }

        return draftPlayerImpl(existingPlayer, minorLeaguePick, true);
    }

    private MinorLeaguePickView draftPlayerImpl(Player existingPlayer, MinorLeaguePickView minorLeaguePick, boolean setupNextPick) throws IllegalMinorLeagueDraftPickException
    {
        PlayerSeason currentPlayerSeason = playerSeasonService.getCurrentPlayerSeason(existingPlayer.getId());
        if (currentPlayerSeason == null) {
            currentPlayerSeason = playerSeasonService.createPlayerSeason(existingPlayer.getId(), LeagueUtil.SEASON);
        }

        if (currentPlayerSeason.getTeamId() != null && currentPlayerSeason.getTeamId() != 0) {
            throw new IllegalMinorLeagueDraftPickException(String.format("%s is already on a team and cannot be drafted", existingPlayer.getName()));
        }

        currentPlayerSeason = playerSeasonService.switchTeam(currentPlayerSeason, currentPlayerSeason.getTeamId(), minorLeaguePick.getOwningTeamId());
        currentPlayerSeason = playerSeasonService.switchFantasyPosition(currentPlayerSeason, currentPlayerSeason.getFantasyPosition(), Position.MINORLEAGUES);
        currentPlayerSeason = playerSeasonService.upsert(currentPlayerSeason);

        minorLeaguePick.setPlayerId(currentPlayerSeason.getPlayerId());
        if (minorLeaguePick.getIsSkipped() != null) {
            minorLeaguePick.setIsSkipped(false);
        }
        minorLeaguePickService.upsert(minorLeaguePick);
        PlayerView playerView = PlayerView.from(existingPlayer);
        playerView.setCurrentSeason(PlayerSeasonView.from(currentPlayerSeason));
        minorLeaguePick.setPlayerView(playerView);
        if (setupNextPick) {
            minorLeaguePick.setNextPick(setupNextPick(minorLeaguePick));
        }

        sendEmail(composePlayerDraftedEmail(minorLeaguePick));

        scheduler.cancel(MinorLeaguePick.class, minorLeaguePick.getId());

        return minorLeaguePick;
    }

    @Override
    public MinorLeaguePickView skipPick(long pickId) {
        MinorLeaguePick pick = minorLeaguePickService.getById(pickId);
        return skipPickImpl(pick);
    }

    private MinorLeaguePickView skipPickImpl(MinorLeaguePick pick) {
        if (pick.getPlayerId() == null) {
            pick.setIsSkipped(true);
        }
        pick.setDeadlineUTC(null);
        MinorLeaguePickView view = MinorLeaguePickView.from(minorLeaguePickService.upsert(pick));
        view.setOriginalTeam(teamService.getFantasyTeamMap().get(view.getOriginalTeamId()));
        view.setOwningTeam(teamService.getFantasyTeamMap().get(view.getOwningTeamId()));
        view.setSwapTeam(teamService.getFantasyTeamMap().get(view.getSwapTeamId()));
        view.setNextPick(setupNextPick(view));

        sendEmail(composePickSkippedEmail(view));
        return view;
    }

    @Nullable
    private MinorLeaguePickView setupNextPick(MinorLeaguePick pick) {
        MinorLeaguePick nextPick = minorLeaguePickService.getMinorLeaguePickByOverallAndSeason(pick.getOverallPick() + 1, LeagueUtil.SEASON);
        if (nextPick == null) {
            return null;
        }
        nextPick.setDeadlineUTC(DateTime.now(DateTimeZone.UTC).plusMinutes(EnvironmentUtility.getInstance().getDraftPickExpirationMinutes()));
        nextPick = minorLeaguePickService.upsert(nextPick);

        scheduler.schedule(nextPick, getRunnable(nextPick));

        MinorLeaguePickView nextPickView = MinorLeaguePickView.from(nextPick);
        nextPickView.setOwningTeam(teamService.getFantasyTeamMap().get(nextPick.getOwningTeamId()));
        nextPickView.setOriginalTeam(teamService.getFantasyTeamMap().get(nextPick.getOriginalTeamId()));
        nextPickView.setSwapTeam(teamService.getFantasyTeamMap().get(nextPick.getSwapTeamId()));
        return nextPickView;
    }

    @Override
    public MinorLeagueDraftView adminDraft(MinorLeagueDraftAdminView view) throws Exception {
        if (!view.getAssignPlayerToPick() && !view.getUndoPick() && !view.getReschedulePick() && !view.getSkipPick() &&
                !view.getStopSkipper() && !view.getSwapPicks()) {
            throw new Exception("No admin flags were set");
        }
        MinorLeaguePick minorLeaguePick = minorLeaguePickService.getById(view.getPickId());
        if (minorLeaguePick == null) {
            throw new Exception("Could not find minor league pick with id " + view.getPickId());
        }
        if (view.getAssignPlayerToPick()) {
            if (view.getPlayerId() == null) {
                throw new Exception("Attempt to set player on pick without supplying player id");
            }
            Player player = playerService.getById(view.getPlayerId());
            draftPlayerImpl(player, MinorLeaguePickView.from(minorLeaguePick), false);
        } else if (view.getUndoPick()) {
            if (minorLeaguePick.getPlayerId() != null) {
                PlayerSeason playerSeason = playerSeasonService.getCurrentPlayerSeason(minorLeaguePick.getPlayerId());
                if (playerSeason == null) {
                    throw new IllegalArgumentException("Could not find player season for playerId " + minorLeaguePick.getPlayerId());
                }
                playerSeason = playerSeasonService.switchFantasyPosition(playerSeason, playerSeason.getFantasyPosition(), null);
                playerSeason = playerSeasonService.switchTeam(playerSeason, playerSeason.getTeamId(), null);
                playerSeasonService.upsert(playerSeason);
            }

            minorLeaguePick.setIsSkipped(null);
            minorLeaguePick.setPlayerId(null);
            minorLeaguePick.setDeadlineUTC(null);

            minorLeaguePickService.upsert(minorLeaguePick);

            scheduler.cancelAll(MinorLeaguePick.class);

            MinorLeaguePickView pickView = MinorLeaguePickView.from(minorLeaguePick);
            pickView.setOwningTeam(teamService.getFantasyTeamMap().get(minorLeaguePick.getOwningTeamId()));
            sendEmail(composePickUndoneEmail(pickView));
        } else if (view.getReschedulePick()) {
            minorLeaguePick.setDeadlineUTC(view.getDeadlineUtc());

            scheduler.cancelAll(MinorLeaguePick.class);

            minorLeaguePickService.upsert(minorLeaguePick);

            scheduler.schedule(minorLeaguePick, getRunnable(minorLeaguePick));

            MinorLeaguePickView pickView = MinorLeaguePickView.from(minorLeaguePick);
            pickView.setOwningTeam(teamService.getFantasyTeamMap().get(minorLeaguePick.getOwningTeamId()));
            sendEmail(composePickRescheduledEmail(pickView));
        } else if (view.getSkipPick()) {
            skipPickImpl(minorLeaguePick);
        } else if (view.getStopSkipper()) {
            scheduler.cancelAll(MinorLeaguePick.class);
        } else if (view.getSwapPicks()) {
            if (view.getPickId1() == null || view.getPickId2() == null) {
                throw new Exception("One of pick ids to swap was missing");
            }
            MinorLeaguePick pick1 = minorLeaguePickService.getById(view.getPickId1());
            MinorLeaguePick pick2 = minorLeaguePickService.getById(view.getPickId2());

            if (pick1 == null || pick2 == null) {
                throw new Exception("Could not find pick to swap for one or both of supplied ids");
            }

            int overall = pick1.getOverallPick();
            pick1.setOverallPick(pick2.getOverallPick());
            pick2.setOverallPick(overall);

            minorLeaguePickService.upsert(pick1);
            minorLeaguePickService.upsert(pick2);
        }

        return getMinorLeagueDraft(LeagueUtil.SEASON);
    }

    private Runnable getRunnable(MinorLeaguePick nextPick) {
        final long nextPickId = nextPick.getId();
        return () -> {
            try {
                skipPick(nextPickId);
            } catch (Exception e) {
                logger.error("Error skipping pick via scheduler: " + e.getMessage());
            }
        };
    }

    private EmailRequest composePlayerDraftedEmail(MinorLeaguePickView pick) {
        String subject = pick.getPlayerView().getName() + " was drafted by " + pick.getOwningTeam().getName();
        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(
                        HtmlObject.of(HtmlTag.DIV).body(subject));
        addNextPickObject(htmlObject, pick);
        htmlObject.child(linkObject());
        return new EmailRequest(null, subject, htmlObject);
    }

    private EmailRequest composePickSkippedEmail(MinorLeaguePickView pick) {
        String subject = pick.getOwningTeam().getName() + " has skipped their pick";
        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(
                        HtmlObject.of(HtmlTag.DIV).body(subject));
        addNextPickObject(htmlObject, pick);
        htmlObject.child(linkObject());
        return new EmailRequest(null, subject, htmlObject);
    }

    private static EmailRequest composePickRescheduledEmail(MinorLeaguePickView pick) {
        String subject = "Current pick for " + pick.getOwningTeam().getName() + " has been rescheduled";
        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(
                        HtmlObject.of(HtmlTag.DIV).body(subject));
        htmlObject.child(
                HtmlObject.of(HtmlTag.DIV).body("The new deadline for this pick is " + pick.getDeadlineUTC().withZone(DateTimeZone.getDefault()).toString(dateTimeFormatter))
        );
        htmlObject.child(linkObject());
        return new EmailRequest(null, subject, htmlObject);
    }

    private static EmailRequest composePickUndoneEmail(MinorLeaguePickView pick) {
        String subject = "Pick for " + pick.getOwningTeam().getName() + " has been rolled back";
        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(
                        HtmlObject.of(HtmlTag.DIV).body(subject));
        htmlObject.child(
                HtmlObject.of(HtmlTag.DIV).body("Any drafted player is now a free agent. If this too is incorrect, please contact Ari and he will fix it.")
        );
        htmlObject.child(linkObject());
        return new EmailRequest(null, subject, htmlObject);
    }

    private static void addNextPickObject(HtmlObject htmlObject, MinorLeaguePickView pick) {
        if (pick.getNextPick() != null) {
            String nextPickMessage = pick.getNextPick().getOwningTeam().getName() + " is up next. ";
            if (pick.getNextPick().getDeadlineUTC() != null) {
                nextPickMessage += "The deadline for their pick is " + pick.getNextPick().getDeadlineUTC().withZone(DateTimeZone.getDefault()).toString(dateTimeFormatter);
            }
            htmlObject.child(
                    HtmlObject.of(HtmlTag.DIV).body(nextPickMessage)
            );
        }
    }

    private static HtmlObject linkObject() {
        return HtmlObject.of(HtmlTag.DIV).child(
                        HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/minorLeagueDraft").body("Click here to see it in the app"));
    }

    private void sendEmail(EmailRequest emailRequest) {
        List<String> emails = $.of(userService.getAllUsers()).toList(User::getEmail);
        emailRequest.setToAddresses(emails);
        emailService.sendEmail(emailRequest);
    }
}
