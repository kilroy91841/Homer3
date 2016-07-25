package com.homer.service;

import com.homer.type.Team;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/15/16.
 */
public interface ITeamService extends IIdService<Team> {

    Map<Long, Team> getFantasyTeamMap();

    List<Team> getTeams();
}
