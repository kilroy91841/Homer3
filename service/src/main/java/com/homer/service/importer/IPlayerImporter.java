package com.homer.service.importer;

import com.homer.type.Player;
import com.homer.type.view.PlayerView;

import java.util.List;

/**
 * Created by arigolub on 4/20/16.
 */
public interface IPlayerImporter {

    PlayerView updatePlayer(Player player);

    List<PlayerView> update40ManRoster(long teamId);
}
