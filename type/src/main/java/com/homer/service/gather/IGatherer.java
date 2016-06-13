package com.homer.service.gather;

import com.google.common.collect.Lists;
import com.homer.type.Player;
import com.homer.type.Team;
import com.homer.type.view.PlayerView;
import com.homer.type.view.TeamView;
import com.homer.util.core.$;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/16/16.
 */
public interface IGatherer {

    Map<Long, Team> getFantasyTeamMap();

    //region players

    List<PlayerView> gatherPlayers();
    List<PlayerView> gatherPlayers(Collection<Player> players);
    @Nullable
    default PlayerView gatherPlayer(Player player) {
        return $.of(this.gatherPlayers(Lists.newArrayList(player))).first();
    }

    List<PlayerView> gatherPlayersByIds(Collection<Long> playerIds);
    @Nullable
    default PlayerView gatherPlayerById(long id) {
        return $.of(this.gatherPlayersByIds(Lists.newArrayList(id))).first();
    }

    List<PlayerView> gatherPlayersByTeamId(long teamId, boolean onlyMinorLeaguers);

    //endregion

    //region teams

    List<TeamView> gatherTeamsByIds(Collection<Long> teamIds);
    @Nullable
    default TeamView gatherTeamById(long teamId) {
        return $.of(this.gatherTeamsByIds(Lists.newArrayList(teamId))).first();
    }

    //endregion

}
