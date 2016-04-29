package com.homer.service.full;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.ITeamService;
import com.homer.service.PlayerSeasonService;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.Team;
import com.homer.type.view.PlayerSeasonView;
import com.homer.type.view.TeamView;
import com.homer.util.core.$;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 4/29/16.
 */
public class FullTeamService implements IFullTeamService {

    private IPlayerSeasonService playerSeasonService;
    private ITeamService teamService;

    public FullTeamService(IPlayerSeasonService playerSeasonService, ITeamService teamService) {
        this.playerSeasonService = playerSeasonService;
        this.teamService = teamService;
    }

    @Override
    public List<TeamView> getTeamSalaries() {
        List<Team> teams = teamService.getTeams();
        Map<Long, Team> teamMap = $.of(teams).toIdMap();

        List<PlayerSeason> allPlayers = $.of(playerSeasonService.getActivePlayers())
                .filterToList(ps -> ps.getTeamId() != null);
        ListMultimap<Long, PlayerSeason> teamToPlayers = Multimaps.index(allPlayers, PlayerSeason::getTeamId);

        List<TeamView> teamViews = Lists.newArrayList();
        for(Long teamId : teamToPlayers.keySet()) {
            TeamView tv = TeamView.from(teamMap.get(teamId));
            List<PlayerSeason> players = teamToPlayers.get(teamId);
            int salary = $.of(players)
                    .filter(ps -> ps.getFantasyPosition() != Position.DISABLEDLIST && ps.getFantasyPosition() != Position.MINORLEAGUES)
                    .reduceToInt(ps -> ps.getSalary());
            tv.setSalary(salary);
            teamViews.add(tv);
        }
        return teamViews;
    }
}
