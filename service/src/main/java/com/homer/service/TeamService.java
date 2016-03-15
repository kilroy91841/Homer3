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

//    public TeamView getTeamById(long teamId) {
//        TeamView teamView = new TeamView();
//        List<PlayerView> allPlayers = playerService.getPlayersByTeam(teamId);
//        teamView.setTeam(this.getTeamByIdImpl(teamId));
//        teamView.setMajorLeaguers(allPlayers.stream().filter(p -> !p.getCurrentSeason().getIsMinorLeaguer()).collect(Collectors.toList()));
//        teamView.setMinorLeaguers(allPlayers.stream().filter(p -> p.getCurrentSeason().getIsMinorLeaguer()).collect(Collectors.toList()));
//        teamView.setSalary(
//                teamView.getMajorLeaguers()
//                        .stream()
//                        .map(pv -> pv.getCurrentSeason().getSalary())
//                        .reduce(Integer::sum)
//                        .get()
//        );
//        return teamView;
//    }
}
