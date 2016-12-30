package com.homer.service;

import com.google.common.collect.Sets;
import com.homer.service.full.IFullHistoryService;
import com.homer.service.full.IFullTradeService;
import com.homer.type.SeptemberStanding;
import com.homer.type.Trade;
import com.homer.type.history.HistoryDraftDollar;
import com.homer.type.view.DraftDollarView;
import com.homer.util.core.$;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 8/5/16.
 */
public class FullHistoryService implements IFullHistoryService {

    private IDraftDollarService draftDollarService;
    private IFullTradeService fullTradeService;
    private ITeamService teamService;
    private IStandingService standingService;

    public FullHistoryService(IDraftDollarService draftDollarService,
                              IFullTradeService fullTradeService,
                              ITeamService teamService,
                              IStandingService standingService) {
        this.draftDollarService = draftDollarService;
        this.fullTradeService = fullTradeService;
        this.teamService = teamService;
        this.standingService = standingService;
    }

    @Override
    public DraftDollarView getDraftDollarHistory(long draftDollarId) {
        DraftDollarView draftDollarView = DraftDollarView.from(draftDollarService.getById(draftDollarId));
        List<HistoryDraftDollar> historyDraftDollars = draftDollarService.getHistories(draftDollarId);
        List<Long> tradeIds = $.of(historyDraftDollars).filter(hdd -> hdd.getTradeId() != null).toList(HistoryDraftDollar::getTradeId);
        Map<Long, Trade> trades = $.of(fullTradeService.getFullTrades(tradeIds)).toIdMap();
        Map<Long, SeptemberStanding> septemberStandingsById =
                $.of(standingService.getSeptemberStandingsByIds($.of(historyDraftDollars).filter(x -> x.getSeptemberStandingId() != null).toList(HistoryDraftDollar::getSeptemberStandingId)))
                .toIdMap();

        List<DraftDollarView> historyViews = $.of(historyDraftDollars)
                .toList(hdd -> {
                    DraftDollarView view = DraftDollarView.from(hdd);
                    view.setHistoryId(hdd.getHistoryId());
                    view.setTeam(teamService.getFantasyTeamMap().get(view.getTeamId()));
                    if (view.getTradeId() != null) {
                        view.setTrade(trades.get(view.getTradeId()));
                    }
                    if (hdd.getSeptemberStandingId() != null) {
                        view.setSeptemberStanding(septemberStandingsById.get(hdd.getSeptemberStandingId()));
                    }
                    return view;
                });
        draftDollarView.setTeam(teamService.getFantasyTeamMap().get(draftDollarView.getTeamId()));
        draftDollarView.setHistoryDraftDollars(historyViews);
        return draftDollarView;
    }
}
