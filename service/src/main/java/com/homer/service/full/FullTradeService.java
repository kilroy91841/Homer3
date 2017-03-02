package com.homer.service.full;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.service.*;
import com.homer.service.auth.IUserService;
import com.homer.service.auth.User;
import com.homer.service.gather.IGatherer;
import com.homer.type.*;
import com.homer.type.view.DraftDollarView;
import com.homer.type.view.MinorLeaguePickView;
import com.homer.type.view.TradesView;
import com.homer.util.EnvironmentUtility;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import com.homer.util.core.Tuple;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.homer.service.full.FullTeamService.calculateActiveSalary;

/**
 * Created by arigolub on 3/20/16.
 */
public class FullTradeService implements IFullTradeService {

    private final ITradeService tradeService;
    private final ITradeElementService tradeElementService;
    private final IPlayerSeasonService playerSeasonService;
    private final IDraftDollarService draftDollarService;
    private final IMinorLeaguePickService minorLeaguePickService;
    private final IGatherer gatherer;
    private IEmailService emailService;
    private IUserService userService;

    public FullTradeService(ITradeService tradeService, IMinorLeaguePickService minorLeaguePickService,
                            IDraftDollarService draftDollarService, IPlayerSeasonService playerSeasonService,
                            ITradeElementService tradeElementService, IGatherer gatherer,
                            IEmailService emailService, IUserService userService) {
        this.tradeService = tradeService;
        this.minorLeaguePickService = minorLeaguePickService;
        this.draftDollarService = draftDollarService;
        this.playerSeasonService = playerSeasonService;
        this.tradeElementService = tradeElementService;
        this.gatherer = gatherer;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public Trade proposeTrade(Trade inTrade) {
        List<TradeElement> tradeElements = Lists.newArrayList();
        inTrade.setTeam1Id(inTrade.getTeam1().getId());
        inTrade.setTeam2Id(inTrade.getTeam2().getId());
        inspectTradeElements(inTrade, tradeElements, Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList());

        Trade trade = new Trade();
        trade.setTeam1Id(inTrade.getTeam1().getId());
        trade.setTeam2Id(inTrade.getTeam2().getId());
        trade.setSeason(LeagueUtil.SEASON);
        trade.setProposedDateUTC(DateTime.now(DateTimeZone.UTC));
        trade.setTradeStatus(EventStatus.IN_PROGRESS);
        trade = tradeService.upsert(trade);

        long tradeId = trade.getId();

        for(TradeElement te : tradeElements) {
            te.setTradeId(tradeId);
            tradeElementService.upsert(te);
        }

        Trade fullTrade = $.of(gatherer.gatherTradesByIds(Lists.newArrayList(tradeId))).first();
        sendProposalEmail(fullTrade);
        return fullTrade;
    }

    @Override
    public Trade rejectTrade(long inTradeId) {
        Trade inTrade = tradeService.getById(inTradeId);
        inTrade.setTradeStatus(EventStatus.DENIED);
        inTrade.setRespondedDateUTC(DateTime.now(DateTimeZone.UTC));

        Trade trade = tradeService.upsert(inTrade);
        Trade fullTrade = $.of(gatherer.gatherTradesByIds(Lists.newArrayList(trade.getId()))).first();
        sendRejectionEmail(fullTrade);
        return fullTrade;
    }

    @Override
    public Trade acceptTrade(long inTradeId) {
        return acceptTrade(getFullTrade(inTradeId));
    }

    @Override
    public Trade acceptTrade(Trade inTrade) {
        List<PlayerSeason> playersToUpdate = Lists.newArrayList();
        List<MinorLeaguePick> picksToUpdate = Lists.newArrayList();
        List<DraftDollar> dollarsToUpdate = Lists.newArrayList();

        inspectTradeElements(inTrade, Lists.newArrayList(), playersToUpdate, picksToUpdate, dollarsToUpdate);

        inTrade.setTradeStatus(EventStatus.SUCCESSFUL);
        inTrade.setRespondedDateUTC(DateTime.now(DateTimeZone.UTC));
        Trade trade = tradeService.upsert(inTrade);
        long tradeId = trade.getId();

        playersToUpdate.forEach(playerSeasonService::upsert);
        picksToUpdate.forEach(minorLeaguePickService::upsert);
        dollarsToUpdate.forEach(dd -> {
            dd.setTradeId(tradeId);
            draftDollarService.upsert(dd);
        });

        Trade fullTrade = $.of(gatherer.gatherTradesByIds(Lists.newArrayList(tradeId))).first();
        sendAcceptanceEmail(fullTrade);
        return fullTrade;
    }

    @Override
    public Trade cancelTrade(long inTradeId) {
        Trade inTrade = tradeService.getById(inTradeId);
        inTrade.setTradeStatus(EventStatus.CANCELLED);

        Trade trade = tradeService.upsert(inTrade);

        Trade fullTrade = $.of(gatherer.gatherTradesByIds(Lists.newArrayList(trade.getId()))).first();
        sendCancelEmail(fullTrade);
        return fullTrade;
    }

    @Override
    public List<Trade> getFullTrades(Collection<Long> tradeIds) {
        return hydrateTrades(tradeService.getByIds(tradeIds));
    }

    @Override
    public TradesView getTradesForTeam(long teamId) {
        List<Trade> trades = gatherer.gatherTradesByIds($.of(tradeService.getTradesByTeamId(teamId)).toIdList());
        TradesView view = new TradesView();
        view.getActiveTrades().addAll($.of(trades).filterToList(t -> t.getTradeStatus() == EventStatus.IN_PROGRESS));
        view.getCompletedTrades().addAll($.of(trades).filterToList(t -> t.getTradeStatus() == EventStatus.SUCCESSFUL));
        view.getRejectedTrades().addAll($.of(trades).filterToList(t -> t.getTradeStatus() == EventStatus.DENIED));
        view.getCancelledTrades().addAll($.of(trades).filterToList(t -> t.getTradeStatus() == EventStatus.CANCELLED));
        return view;
    }

    // region helpers

    private List<Trade> hydrateTrades(Collection<Trade> trades) {
        List<Long> tradeIds = $.of(trades).toIdList();
        Map<Long, List<TradeElement>> tradeElements =
                $.of(tradeElementService.getTradeElementsByTradeIds(tradeIds))
                        .groupBy(TradeElement::getTradeId);
        return $.of(trades)
                .toList(trade -> {
                    trade.getTradeElements().addAll(tradeElements.get(trade.getId()));
                    return trade;
                });
    }

    private void inspectTradeElements(Trade inTrade,
                                      List<TradeElement> tradeElements,
                                      List<PlayerSeason> playersToUpdate,
                                      List<MinorLeaguePick> picksToUpdate,
                                      List<DraftDollar> dollarsToUpdate) {
        Map<Long, List<PlayerSeason>> updatedPlayerSeasons = Maps.newHashMap();

        inTrade.getTradeElements().forEach(tev -> {
            long teamFromId = tev.getTeamFromId();
            long teamToId = tev.getTeamToId();

            TradeElement te = new TradeElement();
            te.setTeamFromId(teamFromId);
            te.setTeamToId(teamToId);

            if (tev.getMinorLeaguePickId() != null) {
                MinorLeaguePick updatedPick;
                if (tev.getSwapTrade() != null && tev.getSwapTrade()) {
                    updatedPick = minorLeaguePickService.transferSwapRights(teamFromId,
                            teamToId, tev.getMinorLeaguePickId());
                    te.setSwapTrade(true);
                } else {
                    updatedPick = minorLeaguePickService.transferPick(teamFromId,
                            teamToId, tev.getMinorLeaguePickId());
                    te.setSwapTrade(false);
                }
                picksToUpdate.add(updatedPick);
                te.setMinorLeaguePickId(updatedPick.getId());
            } else if (tev.getDraftDollarId() != null) {
                if (tev.getDraftDollarAmount() == null) {
                    throw new IllegalArgumentException("You must select draft dollar amount");
                }
                Tuple<DraftDollar> pair =
                        draftDollarService.transferMoney(teamFromId, teamToId,
                                tev.getDraftDollarId(), tev.getDraftDollarAmount());
                dollarsToUpdate.add(pair.getLeft());
                dollarsToUpdate.add(pair.getRight());
                te.setDraftDollarId(pair.getLeft().getId());
                te.setDraftDollarAmount(tev.getDraftDollarAmount());
            } else if (tev.getPlayerId() != null) {
                PlayerSeason updatedPlayer = playerSeasonService.switchTeam(tev.getPlayerId(), LeagueUtil.SEASON,
                        teamFromId, teamToId);
                playersToUpdate.add(updatedPlayer);
                te.setPlayerId(updatedPlayer.getPlayerId());

                List<PlayerSeason> teamToPlayers = updatedPlayerSeasons.getOrDefault(teamToId, Lists.newArrayList());
                teamToPlayers.add(updatedPlayer);
                updatedPlayerSeasons.put(teamToId, teamToPlayers);
            } else {
                throw new IllegalArgumentException("Trade element had no tradable object");
            }

            tradeElements.add(te);
        });

        if (updatedPlayerSeasons.keySet().size() > 0) {
            long team1Id = inTrade.getTeam1Id();
            long team2Id = inTrade.getTeam2Id();

            Map<Long, List<PlayerSeason>> teamPlayerSeasons =
                    $.of(playerSeasonService.getPlayerSeasonsByTeamIds(Lists.newArrayList(team1Id, team2Id), LeagueUtil.SEASON))
                            .groupBy(PlayerSeason::getTeamId);

            List<PlayerSeason> team1Players = teamPlayerSeasons.get(team1Id);
            List<PlayerSeason> team2Players = teamPlayerSeasons.get(team2Id);
            List<PlayerSeason> toTeam1Players = updatedPlayerSeasons.getOrDefault(team1Id, Lists.newArrayList());
            List<PlayerSeason> toTeam2Players = updatedPlayerSeasons.getOrDefault(team2Id, Lists.newArrayList());

            List<PlayerSeason> team1AfterTrade = $.of(team1Players).filterToList(ps -> !$.of(toTeam2Players).toList(PlayerSeason::getPlayerId).contains(ps.getPlayerId()));
            team1AfterTrade.addAll(toTeam1Players);

            List<PlayerSeason> team2AfterTrade = $.of(team2Players).filterToList(ps -> !$.of(toTeam1Players).toList(PlayerSeason::getPlayerId).contains(ps.getPlayerId()));
            team2AfterTrade.addAll(toTeam2Players);

            int team1Salary = calculateActiveSalary(team1AfterTrade);
            int team2Salary = calculateActiveSalary(team2AfterTrade);

            if (team1Salary > DraftDollarService.MLB_DRAFT_DOLLAR_MAX ||
                    team2Salary > DraftDollarService.MLB_DRAFT_DOLLAR_MAX) {
                throw new IllegalArgumentException("Cannot process trade as it would put one of the teams over the salary limit");
            }
        }
    }

    private void sendProposalEmail(Trade trade) {
        String subject = "New Trade Proposal";
        String firstLine = "A new trade has been proposed by " + trade.getTeam1().getName() + ":";
        sendTradeEmail(subject, firstLine, trade);
    }

    private void sendRejectionEmail(Trade trade) {
        String subject = "Trade Rejected";
        String firstLine = "A trade has been rejected by " + trade.getTeam2().getName() + ":";
        sendTradeEmail(subject, firstLine, trade);
    }

    private void sendAcceptanceEmail(Trade trade) {
        String subject = "Trade Accepted";
        String firstLine = "A trade between " + trade.getTeam1().getName() + " and " + trade.getTeam2().getName() + " has been accepted:";
        sendTradeEmail(subject, firstLine, trade);
    }

    private void sendCancelEmail(Trade trade) {
        String subject = "Trade Cancelled";
        String firstLine = "A trade has been cancelled by " + trade.getTeam1().getName() + ":";
        sendTradeEmail(subject, firstLine, trade);
    }

    private void sendTradeEmail(String subject, String firstLine, Trade trade) {
        Collection<TradeElement> tradeElements = trade.getTradeElements();

        HtmlObject html = HtmlObject.of(HtmlTag.DIV);
        html.child(HtmlObject.of(HtmlTag.P).body(firstLine));
        HtmlObject teamList = HtmlObject.of(HtmlTag.UL);
        HtmlObject team1List = HtmlObject.of(HtmlTag.LI).child(HtmlObject.of(HtmlTag.SPAN).body(trade.getTeam1().getName() + " gets: "));
        HtmlObject team2List = HtmlObject.of(HtmlTag.LI).child(HtmlObject.of(HtmlTag.SPAN).body(trade.getTeam2().getName() + " gets: "));

        List<TradeElement> team1Elements = $.of(tradeElements).filterToList(te -> te.getTeamToId() == trade.getTeam1().getId());
        List<TradeElement> team2Elements = $.of(tradeElements).filterToList(te -> te.getTeamToId() == trade.getTeam2().getId());

        addTradeElements(team1Elements, team1List);
        addTradeElements(team2Elements, team2List);

        teamList.child(team1List);
        teamList.child(team2List);

        html.child(teamList);
        html.child(HtmlObject.of(HtmlTag.A).withProperty("href", EnvironmentUtility.getInstance().getUrl() + "/trade").body("Click here to see it in the app"));

        EmailRequest emailRequest = new EmailRequest(getTradeEmails(trade), subject, html);

        emailService.sendEmail(emailRequest);
    }

    private void addTradeElements(List<TradeElement> tradeElements, HtmlObject tradeElementList) {
        if (tradeElements.size() > 0) {
            HtmlObject innerList = HtmlObject.of(HtmlTag.UL);

            for (TradeElement te : tradeElements) {
                String text = null;
                if (te.getPlayer() != null) {
                    text = te.getPlayer().getName();
                } else if (te.getMinorLeaguePick() != null) {
                    MinorLeaguePickView view = MinorLeaguePickView.from(te.getMinorLeaguePick());
                    text = view.getText();
                    if (te.getSwapTrade()) {
                        text += " (Right to swap)";
                    }
                } else if (te.getDraftDollar() != null) {
                    DraftDollarView view = DraftDollarView.from(te.getDraftDollar());
                    text = "$" + te.getDraftDollarAmount() + " of " + view.getText();
                }
                innerList.child(HtmlObject.of(HtmlTag.LI).body(text));
            }
            tradeElementList.child(innerList);
        }
    }

    private List<String> getTradeEmails(Trade trade) {
        List<User> usersToEmail = Lists.newArrayList();
        if (trade.getTradeStatus() == EventStatus.SUCCESSFUL) {
            usersToEmail.addAll(userService.getAllUsers());
        } else {
            usersToEmail.addAll(userService.getUsersForTeam(trade.getTeam1().getId()));
            usersToEmail.addAll(userService.getUsersForTeam(trade.getTeam2().getId()));
        }
        return $.of(usersToEmail).toList(User::getEmail);
    }

    // endregion
}
