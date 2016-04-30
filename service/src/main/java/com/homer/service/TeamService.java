package com.homer.service;

import com.homer.data.common.ITeamRepository;
import com.homer.type.Team;

import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/6/16.
 */
public class TeamService implements ITeamService {

    private ITeamRepository repo;

    public TeamService(ITeamRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Team> getTeams() {
        return repo.getAll();
    }

    @Override
    public List<Team> getTeamsByIds(Collection<Long> ids) {
        return repo.getByIds(ids);
    }
}
