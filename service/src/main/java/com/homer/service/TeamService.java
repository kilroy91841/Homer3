package com.homer.service;

import com.homer.data.common.ITeamRepository;
import com.homer.type.Team;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/6/16.
 */
public class TeamService extends BaseIdService<Team> implements ITeamService {

    private ITeamRepository repo;

    public TeamService(ITeamRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public List<Team> getTeams() {
        return repo.getAll();
    }
}
