package com.homer.service;

import com.homer.data.common.ITeamRepository;
import com.homer.type.Team;
import com.homer.type.history.HistoryTeam;
import com.homer.util.core.$;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/6/16.
 */
public class TeamService extends BaseVersionedIdService<Team, HistoryTeam> implements ITeamService {

    private static Map<Long, Team> FANTASY_TEAM_MAP;

    private ITeamRepository repo;

    public TeamService(ITeamRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public Map<Long, Team> getFantasyTeamMap() {
        if (FANTASY_TEAM_MAP == null) {
            FANTASY_TEAM_MAP = $.of(getTeams()).toIdMap();
        }
        return FANTASY_TEAM_MAP;
    }

    @Override
    public List<Team> getTeams() {
        return repo.getAll();
    }
}
