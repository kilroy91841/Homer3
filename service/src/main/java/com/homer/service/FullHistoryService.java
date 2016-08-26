package com.homer.service;

import com.homer.service.full.IFullHistoryService;
import com.homer.service.full.IFullTradeService;
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

    public FullHistoryService(IDraftDollarService draftDollarService,
                              IFullTradeService fullTradeService,
                              ITeamService teamService) {
        this.draftDollarService = draftDollarService;
        this.fullTradeService = fullTradeService;
        this.teamService = teamService;
    }

    @Override
    public DraftDollarView getDraftDollarHistory(long draftDollarId) {
        DraftDollarView draftDollarView = DraftDollarView.from(draftDollarService.getById(draftDollarId));
        List<HistoryDraftDollar> historyDraftDollars = draftDollarService.getHistories(draftDollarId);
        List<Long> tradeIds = $.of(historyDraftDollars).filter(hdd -> hdd.getTradeId() != null).toList(HistoryDraftDollar::getTradeId);
        Map<Long, Trade> trades = $.of(fullTradeService.getFullTrades(tradeIds)).toIdMap();

        List<DraftDollarView> historyViews = $.of(historyDraftDollars)
                .toList(hdd -> {
                    DraftDollarView view = DraftDollarView.from(hdd);
                    view.setHistoryId(hdd.getHistoryId());
                    view.setTeam(teamService.getFantasyTeamMap().get(view.getTeamId()));
                    if (view.getTradeId() != null) {
                        view.setTrade(trades.get(view.getTradeId()));
                    }
                    return view;
                });
        draftDollarView.setTeam(teamService.getFantasyTeamMap().get(draftDollarView.getTeamId()));
        draftDollarView.setHistoryDraftDollars(historyViews);
        return draftDollarView;
    }
}
