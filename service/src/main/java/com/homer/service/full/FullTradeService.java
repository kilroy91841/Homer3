package com.homer.service.full;

import com.google.common.collect.Lists;
import com.homer.service.*;
import com.homer.type.*;
import com.homer.util.LeagueUtil;
import com.homer.util.core.Tuple;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;

/**
 * Created by arigolub on 3/20/16.
 */
public class FullTradeService implements IFullTradeService {

    private final ITradeService tradeService;
    private final ITradeElementService tradeElementService;
    private final IPlayerSeasonService playerSeasonService;
    private final IDraftDollarService draftDollarService;
    private final IMinorLeaguePickService minorLeaguePickService;

    public FullTradeService(ITradeService tradeService, IMinorLeaguePickService minorLeaguePickService,
                            IDraftDollarService draftDollarService, IPlayerSeasonService playerSeasonService,
                            ITradeElementService tradeElementService) {
        this.tradeService = tradeService;
        this.minorLeaguePickService = minorLeaguePickService;
        this.draftDollarService = draftDollarService;
        this.playerSeasonService = playerSeasonService;
        this.tradeElementService = tradeElementService;
    }

    @Override
    public boolean validateAndProcess(Trade inTrade) {
        List<TradeElement> tradeElements = Lists.newArrayList();

        List<PlayerSeason> playersToUpdate = Lists.newArrayList();
        List<MinorLeaguePick> picksToUpdate = Lists.newArrayList();
        List<DraftDollar> dollarsToUpdate = Lists.newArrayList();

        inTrade.getTradeElements().forEach(tev -> {
            long teamFromId = tev.getTeamFrom().getId();
            long teamToId = tev.getTeamTo().getId();

            TradeElement te = new TradeElement();
            te.setTeamFromId(teamFromId);
            te.setTeamToId(teamToId);

            if (tev.getMinorLeaguePick() != null) {
                MinorLeaguePick updatedPick;
                if (tev.getSwapTrade() != null && tev.getSwapTrade()) {
                    updatedPick = minorLeaguePickService.transferSwapRights(teamFromId,
                            teamToId, tev.getMinorLeaguePick().getOriginalTeamId(),
                            tev.getMinorLeaguePick().getRound(), tev.getMinorLeaguePick().getSeason());
                    te.setSwapTrade(true);
                } else {
                    updatedPick = minorLeaguePickService.transferPick(teamFromId,
                            teamToId, tev.getMinorLeaguePick().getOriginalTeamId(),
                            tev.getMinorLeaguePick().getRound(), tev.getMinorLeaguePick().getSeason());
                    te.setSwapTrade(false);
                }
                picksToUpdate.add(updatedPick);
                te.setMinorLeaguePickId(updatedPick.getId());
            } else if (tev.getDraftDollar() != null) {
                Tuple<DraftDollar> pair =
                        draftDollarService.transferMoney(teamFromId, teamToId,
                                tev.getDraftDollar().getSeason(), tev.getDraftDollar().getDraftDollarType(),
                                tev.getDraftDollarAmount());
                dollarsToUpdate.add(pair.getLeft());
                dollarsToUpdate.add(pair.getRight());
                te.setDraftDollarId(pair.getLeft().getId());
                te.setDraftDollarAmount(tev.getDraftDollarAmount());
            } else if (tev.getPlayer() != null) {
                PlayerSeason updatedPlayer = playerSeasonService.switchTeam(tev.getPlayer().getId(), LeagueUtil.SEASON,
                        teamFromId, teamToId);
                playersToUpdate.add(updatedPlayer);
                te.setPlayerId(updatedPlayer.getId());
            } else {
                throw new IllegalArgumentException("Trade element had no tradable object");
            }

            tradeElements.add(te);
        });

        Trade trade = new Trade();
        trade.setTeam1Id(inTrade.getTeam1().getId());
        trade.setTeam2Id(inTrade.getTeam2().getId());
        trade.setSeason(LeagueUtil.SEASON);
        trade.setTradeDate(DateTime.now(DateTimeZone.UTC));
        trade = tradeService.upsert(trade);

        for(TradeElement te : tradeElements) {
            te.setTradeId(trade.getId());
            tradeElementService.upsert(te);
        }

        playersToUpdate.forEach(playerSeasonService::upsert);
        picksToUpdate.forEach(minorLeaguePickService::upsert);
        dollarsToUpdate.forEach(draftDollarService::upsert);

        return true;
    }
}
