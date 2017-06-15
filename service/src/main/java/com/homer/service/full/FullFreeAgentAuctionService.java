package com.homer.service.full;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.exception.FreeAgentAuctionBidException;
import com.homer.external.common.IMLBClient;
import com.homer.external.common.mlb.MLBPlayer;
import com.homer.service.*;
import com.homer.service.auth.IUserService;
import com.homer.service.auth.User;
import com.homer.service.schedule.IScheduler;
import com.homer.type.*;
import com.homer.type.view.FreeAgentAuctionAdminView;
import com.homer.type.view.FreeAgentAuctionBidView;
import com.homer.type.view.FreeAgentAuctionView;
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

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 5/8/16.
 */
public class FullFreeAgentAuctionService implements IFullFreeAgentAuctionService {

    final static Logger logger = LoggerFactory.getLogger(FullFreeAgentAuctionService.class);

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("EEEE MM/dd hh:mm a ZZZZ").withZoneUTC();

    private IFreeAgentAuctionService freeAgentAuctionService;
    private IFreeAgentAuctionBidService freeAgentAuctionBidService;
    private IPlayerSeasonService playerSeasonService;
    private IPlayerService playerService;
    private IEmailService emailService;
    private IDraftDollarService draftDollarService;
    private IMLBClient mlbClient;
    private IFullPlayerService fullPlayerService;
    private ITeamService teamService;
    private IScheduler scheduler;
    private IUserService userService;

    public FullFreeAgentAuctionService(IFreeAgentAuctionService freeAgentAuctionService,
                                       IFreeAgentAuctionBidService freeAgentAuctionBidService,
                                       IPlayerSeasonService playerSeasonService,
                                       IPlayerService playerService,
                                       IFullPlayerService fullPlayerService,
                                       IEmailService emailService,
                                       IDraftDollarService draftDollarService,
                                       IMLBClient mlbClient,
                                       ITeamService teamService,
                                       IScheduler scheduler,
                                       IUserService userService) {
        this.freeAgentAuctionService = freeAgentAuctionService;
        this.freeAgentAuctionBidService = freeAgentAuctionBidService;
        this.playerSeasonService = playerSeasonService;
        this.playerService = playerService;
        this.emailService = emailService;
        this.draftDollarService = draftDollarService;
        this.mlbClient = mlbClient;
        this.fullPlayerService = fullPlayerService;
        this.teamService = teamService;
        this.scheduler = scheduler;
        this.userService = userService;
    }

    @Override
    public List<FreeAgentAuctionView> getFreeAgentAuctions() {
        List<FreeAgentAuction> freeAgentAuctions = freeAgentAuctionService.getForSeason(LeagueUtil.SEASON);
        List<FreeAgentAuctionView> views = Lists.newArrayList();
        List<Long> freeAgentAuctionIds = $.of(freeAgentAuctions).toIdList();
        List<FreeAgentAuctionBid> allBids = freeAgentAuctionBidService.getForFreeAgentAuctions(freeAgentAuctionIds);
        Map<Long, List<FreeAgentAuctionBid>> bidMap = $.of(allBids).groupBy(FreeAgentAuctionBid::getFreeAgentAuctionId);
        for (FreeAgentAuction freeAgentAuction : freeAgentAuctions) {
            FreeAgentAuctionView freeAgentAuctionView = FreeAgentAuctionView.from(freeAgentAuction);
            long playerId = freeAgentAuction.getPlayerId();
            Player player = playerService.getById(playerId);
            if (player != null) {
                freeAgentAuctionView.setPlayer(player);
            }

            Long winningTeamId = freeAgentAuction.getWinningTeamId();
            if (winningTeamId != null) {
                freeAgentAuctionView.setWinningTeam(teamService.getFantasyTeamMap().get(winningTeamId));
            }

            freeAgentAuctionView.setRequestingTeam(teamService.getFantasyTeamMap().get(freeAgentAuction.getRequestingTeamId()));
            List<FreeAgentAuctionBid> bids = bidMap.get(freeAgentAuction.getId());
            if (bids != null) {
                bids.stream().sorted((b1, b2) -> b1.getAmount() > b2.getAmount() ? -1 : 1);
                List<FreeAgentAuctionBidView> bidViews = Lists.newArrayList();
                for (FreeAgentAuctionBid bid : bids) {
                    FreeAgentAuctionBidView bidView = FreeAgentAuctionBidView.from(bid);
                    bidView.setTeam(teamService.getFantasyTeamMap().get(bid.getTeamId()));
                    bidViews.add(bidView);
                }
                bidViews.sort((b1, b2) -> b1.getAmount() > b2.getAmount() ? -1 : 1);

                freeAgentAuctionView.setBids(bidViews);
            }
            views.add(freeAgentAuctionView);
        }
        return views;
    }

    @Override
    public FreeAgentAuctionView requestFreeAgentAuction(FreeAgentAuctionView view) throws FreeAgentAuctionBidException {
        Player player = view.getPlayer();
        if (player == null || player.getMlbPlayerId() == null || view.getRequestingTeamId() == 0) {
            throw new FreeAgentAuctionBidException.BadAuctionRequest("Must supply mlbPlayerId");
        }

        FreeAgentAuction freeAgentAuction = new FreeAgentAuction();
        freeAgentAuction.setRequestingTeamId(view.getRequestingTeamId());
        freeAgentAuction.setAuctionStatus(EventStatus.REQUESTED);
        freeAgentAuction.setSeason(LeagueUtil.SEASON);

        Player existingPlayer = playerService.getPlayerByMLBPlayerId(player.getMlbPlayerId());
        if (existingPlayer == null) {
            MLBPlayer mlbPlayer = mlbClient.getPlayer(player.getMlbPlayerId());
            if (mlbPlayer != null) {
                player.setFirstName(mlbPlayer.getFirstName());
                player.setLastName(mlbPlayer.getLastName());
                player.setName(mlbPlayer.getName());
                player.setPosition(EnumUtil.from(Position.class, mlbPlayer.getPositionId()));
                player.setMlbTeamId(mlbPlayer.getTeamId());

                existingPlayer = fullPlayerService.createPlayer(player);
            } else {
                throw new RuntimeException("Could not find MLB player with id " + player.getMlbPlayerId());
            }
        } else {
            PlayerSeason currentPlayerSeason = playerSeasonService.getCurrentPlayerSeason(existingPlayer.getId());
            if (currentPlayerSeason.getTeamId() != null && currentPlayerSeason.getTeamId() > 0) {
                throw new FreeAgentAuctionBidException.BadAuctionRequest("Requested player is not a free agent");
            }
        }

        freeAgentAuction.setPlayerId(existingPlayer.getId());
        freeAgentAuction = freeAgentAuctionService.upsert(freeAgentAuction);

        emailService.sendEmail(
                new EmailRequest(Lists.newArrayList(IEmailService.COMMISSIONER_EMAIL),
                "New Free Agent Auction Request: " + existingPlayer.getName(),
                HtmlObject.of(HtmlTag.DIV)));

        FreeAgentAuctionView freeAgentAuctionView = FreeAgentAuctionView.from(freeAgentAuction);
        freeAgentAuctionView.setPlayer(existingPlayer);
        freeAgentAuctionView.setRequestingTeam(teamService.getFantasyTeamMap().get(freeAgentAuction.getRequestingTeamId()));
        return freeAgentAuctionView;
    }

    @Override
    public FreeAgentAuction denyFreeAgentAuctionRequest(long freeAgentAuctionId, String denyReason) {
        FreeAgentAuction freeAgentAuction = findFreeAgentAuctionOrThrow(freeAgentAuctionId);
        freeAgentAuction.setAuctionStatus(EventStatus.DENIED);
        freeAgentAuction.setDenyReason(denyReason);

        Player player = playerService.getById(freeAgentAuction.getPlayerId());

        List<User> users = userService.getUsersForTeam(freeAgentAuction.getRequestingTeamId());
        List<String> emails = $.of(users).toList(User::getEmail);
        emails.add(IEmailService.COMMISSIONER_EMAIL);

        emailService.sendEmail(new EmailRequest(emails,
                "Free Agent Auction Request Denied",
                HtmlObject.of(HtmlTag.DIV).child(HtmlObject.of(HtmlTag.P).body("The free agent auction request for " +
                player.getName() + " has been denied for the following reason: " + denyReason + ". Contact the commissioner " +
                "if you think this is incorrect."))));

        return freeAgentAuctionService.upsert(freeAgentAuction);
    }

    @Override
    public FreeAgentAuction startFreeAgentAuction(long freeAgentAuctionId) {
        FreeAgentAuction freeAgentAuction = findFreeAgentAuctionOrThrow(freeAgentAuctionId);
        freeAgentAuction.setAuctionStatus(EventStatus.IN_PROGRESS);
        freeAgentAuction.setDeadlineUtc(DateTime.now(DateTimeZone.UTC).plusMinutes(EnvironmentUtility.getInstance().getFreeAgentAuctionExpirationMinutes()));

        scheduleFreeAgentAuction(freeAgentAuction);

        sendFreeAgentAuctionStartEmail(freeAgentAuction);
        return freeAgentAuctionService.upsert(freeAgentAuction);
    }

    private void scheduleFreeAgentAuction(FreeAgentAuction freeAgentAuction) {
        Runnable runnable = () -> {
            try {
                endFreeAgentAuction(freeAgentAuction.getId());
            } catch (Exception e) {
                logger.error("Error ending free agent auction: " + e.getMessage());
            }
        };

        scheduler.schedule(freeAgentAuction, runnable);
    }

    @Override
    public FreeAgentAuction endFreeAgentAuction(long freeAgentAuctionId) throws FreeAgentAuctionBidException {
        FreeAgentAuction freeAgentAuction = findFreeAgentAuctionOrThrow(freeAgentAuctionId);
        if (freeAgentAuction.getAuctionStatus() != EventStatus.IN_PROGRESS) {
            throw new FreeAgentAuctionBidException.AuctionNotInProgress("This auction is not in progress");
        }

        scheduler.cancel(FreeAgentAuction.class, freeAgentAuctionId);

        List<FreeAgentAuctionBid> bids = freeAgentAuctionBidService.getForFreeAgentAuction(freeAgentAuctionId);
        List<FreeAgentAuctionBid> winningBids = Lists.newArrayList();
        int winningAmount = -1;
        for (FreeAgentAuctionBid bid : bids) {
            if (bid.getAmount() > winningAmount) {
                winningBids.clear();
                winningBids.add(bid);
                winningAmount = bid.getAmount();
            } else if (bid.getAmount() == winningAmount) {
                winningBids.add(bid);
            }
        }

        EmailRequest emailRequest;
        if (winningBids.size() == 1) {
            FreeAgentAuctionBid winningBid = winningBids.get(0);

            emailRequest = doWinningBid(freeAgentAuction, winningBid);
        } else if (winningBids.size() == 0) {
            emailRequest = getEmailForNoBids(freeAgentAuction);
        } else {
            emailRequest = getEmailForTie(freeAgentAuction, winningBids);
        }

        freeAgentAuction.setAuctionStatus(EventStatus.COMPLETE);
        freeAgentAuction = freeAgentAuctionService.upsert(freeAgentAuction);

        emailService.sendEmail(emailRequest);
        return freeAgentAuction;
    }

    @Override
    public FreeAgentAuction setWinningBid(long freeAgentAuctionId, long teamId) {
        FreeAgentAuction freeAgentAuction = findFreeAgentAuctionOrThrow(freeAgentAuctionId);
        freeAgentAuction.setAuctionStatus(EventStatus.COMPLETE);

        List<FreeAgentAuctionBid> bids = freeAgentAuctionBidService.getForFreeAgentAuction(freeAgentAuctionId);
        FreeAgentAuctionBid bid = $.of(bids).first(b -> b.getTeamId() == teamId);
        if (bid == null) {
            throw new RuntimeException("Could not find bid for team " + teamId);
        }

        EmailRequest emailRequest = doWinningBid(freeAgentAuction, bid);

        freeAgentAuction = freeAgentAuctionService.upsert(freeAgentAuction);

        emailService.sendEmail(emailRequest);

        return freeAgentAuction;
    }

    @Override
    public FreeAgentAuction cancelAuction(long freeAgentAuctionId) {
        FreeAgentAuction freeAgentAuction = findFreeAgentAuctionOrThrow(freeAgentAuctionId);
        freeAgentAuction.setAuctionStatus(EventStatus.CANCELLED);

        scheduler.cancel(FreeAgentAuction.class, freeAgentAuctionId);

        sendFreeAgentAuctionCancelledEmail(freeAgentAuction);

        return freeAgentAuctionService.upsert(freeAgentAuction);
    }

    @Override
    public FreeAgentAuctionBid bidOnFreeAgentAuction(long freeAgentAuctionId, long teamId, int amount) throws FreeAgentAuctionBidException {
        FreeAgentAuction freeAgentAuction = findFreeAgentAuctionOrThrow(freeAgentAuctionId);
        if (freeAgentAuction.getAuctionStatus() != EventStatus.IN_PROGRESS || DateTime.now().isAfter(freeAgentAuction.getDeadlineUtc())) {
            throw new FreeAgentAuctionBidException.AuctionNotInProgress("This auction is not in progress");
        }

        if (amount < 0) {
            throw new FreeAgentAuctionBidException.InsufficientFunds("Bid amount must be greater than 0");
        }

        List<FreeAgentAuctionBid> bids = freeAgentAuctionBidService.getForFreeAgentAuction(freeAgentAuctionId);
        FreeAgentAuctionBid bid = $.of(bids).first(b -> b.getTeamId() == teamId);
        if (bid == null) {
            bid = new FreeAgentAuctionBid();
            bid.setTeamId(teamId);
            bid.setFreeAgentAuctionId(freeAgentAuctionId);
        }

        DraftDollar draftDollar = getDraftDollarOrThrow(teamId);
        confirmSufficientFunds(draftDollar, amount);

        bid.setAmount(amount);
        bid = freeAgentAuctionBidService.upsert(bid);

        return bid;
    }

    @Override
    public List<FreeAgentAuctionView> adminFreeAgentAuction(FreeAgentAuctionAdminView adminView) {
        long id = adminView.getFreeAgentAuctionId();
        if (adminView.getDenyAuction()) {
            denyFreeAgentAuctionRequest(id, adminView.getDenyReason());
        } else if (adminView.getStartAuction()) {
            startFreeAgentAuction(id);
        } else if (adminView.getWinningTeam()) {
            setWinningBid(id, adminView.getTeamId());
        } else if (adminView.getCancelAuction()) {
            cancelAuction(id);
        } else {
            throw new RuntimeException("No free agent auction admin action was set");
        }
        return getFreeAgentAuctions();
    }

    private FreeAgentAuction findFreeAgentAuctionOrThrow(long freeAgentAuctionId) {
        FreeAgentAuction freeAgentAuction = freeAgentAuctionService.getById(freeAgentAuctionId);
        if (freeAgentAuction == null) {
            throw new RuntimeException("Could not find free agent auction with id " + freeAgentAuctionId);
        }
        return freeAgentAuction;
    }

    private DraftDollar getDraftDollarOrThrow(long teamId) {
        List<DraftDollar> draftDollars = draftDollarService.getDraftDollarsByTeam(teamId);
        DraftDollar draftDollar = $.of(draftDollars).first(dd -> dd.getDraftDollarType() == DraftDollarType.FREEAGENTAUCTION && dd.getSeason() == LeagueUtil.SEASON);
        if (draftDollar == null) {
            throw new RuntimeException("Could not find free agent auction draft dollars for team " + teamId + " and season " + LeagueUtil.SEASON);
        }
        return draftDollar;
    }

    private EmailRequest doWinningBid(FreeAgentAuction freeAgentAuction, FreeAgentAuctionBid winningBid) {
        freeAgentAuction.setWinningTeamId(winningBid.getTeamId());
        freeAgentAuction.setWinningAmount(winningBid.getAmount());

        DraftDollar draftDollar = getDraftDollarOrThrow(winningBid.getTeamId());
        try {
            confirmSufficientFunds(draftDollar, winningBid.getAmount());
        } catch (FreeAgentAuctionBidException.InsufficientFunds e) {
            freeAgentAuction.setWinningTeamId(null);
            freeAgentAuction.setWinningAmount(null);

            return getInsufficientFundsEmail(freeAgentAuction, winningBid);
        }
        draftDollar.setAmount(draftDollar.getAmount() - winningBid.getAmount());
        draftDollarService.upsert(draftDollar);

        PlayerSeason playerSeason = playerSeasonService.switchTeam(freeAgentAuction.getPlayerId(), LeagueUtil.SEASON, null, winningBid.getTeamId());

        playerSeasonService.upsert(playerSeason);

        return getWinningBidEmail(freeAgentAuction, winningBid);
    }

    private static void confirmSufficientFunds(DraftDollar draftDollar, int amount) throws FreeAgentAuctionBidException.InsufficientFunds {
        if (draftDollar.getAmount() < amount) {
            throw new FreeAgentAuctionBidException.InsufficientFunds("Not enough funds to submit bid. Bid: " + amount + ", Available Funds: " + draftDollar.getAmount());
        }
    }

    private void sendFreeAgentAuctionStartEmail(FreeAgentAuction freeAgentAuction) {
        List<String> emails = $.of(userService.getAllUsers()).toList(User::getEmail);

        Player player = playerService.getById(freeAgentAuction.getPlayerId());

        String subject = "New Free Agent Auction: " + player.getName();

        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(HtmlObject.of(HtmlTag.P).body("A new free agent auction has started for " + player.getName() + ". " +
                "The deadline for the auction is " +
                        freeAgentAuction.getDeadlineUtc().withZone(DateTimeZone.getDefault()).toString(dateTimeFormatter)))
                .child(
                        HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/freeAgentAuction").body("Click here to see it in the app")
                );

        emailService.sendEmail(new EmailRequest(emails, subject, htmlObject));
    }

    private EmailRequest getEmailForNoBids(FreeAgentAuction freeAgentAuction) {
        List<String> emails = $.of(userService.getAllUsers()).toList(User::getEmail);

        Player player = playerService.getById(freeAgentAuction.getPlayerId());

        String subject = "Free Agent Auction Ended: " + player.getName();

        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(HtmlObject.of(HtmlTag.P).body("The deadline to bid on " + player.getName() + " has passed and there were no bids. " + player.getName() + " is now a regular free agent."))
                .child(HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/freeAgentAuction").body("Click here to see it in the app"));

        return new EmailRequest(emails, subject, htmlObject);
    }

    private EmailRequest getEmailForTie(FreeAgentAuction freeAgentAuction, List<FreeAgentAuctionBid> winningBids) {
        List<String> emails = $.of(userService.getAllUsers()).toList(User::getEmail);

        Player player = playerService.getById(freeAgentAuction.getPlayerId());

        String subject = "Free Agent Auction Ended: " + player.getName();

        String tiedBids = "The following teams have tied with a bid of " + winningBids.get(0).getAmount() + ": " +
            Joiner.on(", ").join($.of(winningBids).toList(wb -> teamService.getFantasyTeamMap().get(wb.getTeamId()).getName()));

        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(HtmlObject.of(HtmlTag.P).body("The deadline to bid on " + player.getName() + " has passed. " + tiedBids + ". The commissioner will send a follow-up email with the winning team."))
                .child(HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/freeAgentAuction").body("Click here to see it in the app"));

        return new EmailRequest(emails, subject, htmlObject);
    }

    private EmailRequest getInsufficientFundsEmail(FreeAgentAuction freeAgentAuction, FreeAgentAuctionBid winningBid) {
        List<String> emails = $.of(userService.getAllUsers()).toList(User::getEmail);

        Player player = playerService.getById(freeAgentAuction.getPlayerId());

        String subject = "Free Agent Auction Ended: " + player.getName();

        String insufficientFunds = "The winning bid in the free agent auction of " + player.getName() + " had insufficient funds to satisfy " +
                "the amount bid. The commissioner will send a follow-up email with the new winning team.";
        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(HtmlObject.of(HtmlTag.P).body("The deadline to bid on " + player.getName() + " has passed. " + insufficientFunds))
                .child(HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/freeAgentAuction").body("Click here to see it in the app"));

        return new EmailRequest(emails, subject, htmlObject);
    }

    private void sendFreeAgentAuctionCancelledEmail(FreeAgentAuction freeAgentAuction) {
        List<String> emails = $.of(userService.getAllUsers()).toList(User::getEmail);

        Player player = playerService.getById(freeAgentAuction.getPlayerId());

        String subject = "Free Agent Auction Cancelled: " + player.getName();

        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(HtmlObject.of(HtmlTag.P).body("The free agent auction for " + player.getName() + " has been cancelled. "))
                .child(HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/freeAgentAuction").body("Click here to see it in the app"));

        emailService.sendEmail(new EmailRequest(emails, subject, htmlObject));
    }

    private EmailRequest getWinningBidEmail(FreeAgentAuction freeAgentAuction, FreeAgentAuctionBid winningBid) {
        List<String> emails = $.of(userService.getAllUsers()).toList(User::getEmail);

        Player player = playerService.getById(freeAgentAuction.getPlayerId());

        String subject = "Free Agent Auction Ended: " + player.getName();

        String winningTeam = teamService.getFantasyTeamMap().get(winningBid.getTeamId()).getName() + " has won with a winning bid of " + winningBid.getAmount() + ". " +
                "That team should add the player on ESPN and make any necessary roster adjustments.";

        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV)
                .child(HtmlObject.of(HtmlTag.P).body("The free agent auction for " + player.getName() + " has ended. " + winningTeam))
                .child(HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/freeAgentAuction").body("Click here to see it in the app"));

        return new EmailRequest(emails, subject, htmlObject);
    }
}
