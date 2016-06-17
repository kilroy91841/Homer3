package com.homer.service.full;

import com.google.common.collect.Lists;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.view.PlayerSeasonView;
import com.homer.type.view.PlayerView;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by arigolub on 4/17/16.
 */
public class FullPlayerService implements IFullPlayerService {

    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;

    public FullPlayerService(IPlayerService playerService, IPlayerSeasonService playerSeasonService) {
        this.playerService = playerService;
        this.playerSeasonService = playerSeasonService;
    }

    @Override
    public PlayerView createPlayer(Player player, boolean isMinorLeaguer) {
        Player createdPlayer = playerService.createPlayer(player);
        PlayerSeason playerSeason = playerSeasonService.createPlayerSeason(createdPlayer.getId(), LeagueUtil.SEASON, isMinorLeaguer);
        PlayerView view = PlayerView.from(createdPlayer);
        PlayerSeasonView playerSeasonView = PlayerSeasonView.from(playerSeason);
        List<PlayerSeasonView> playerSeasons = Lists.newArrayList(playerSeasonView);
        view.setPlayerSeasons(playerSeasons);
        return view;
    }
}
