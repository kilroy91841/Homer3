package com.homer.data.common;

import com.homer.type.Team;
import com.homer.type.history.HistoryTeam;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;

/**
 * Created by arigolub on 3/15/16.
 */
public interface ITeamRepository extends IVersionedRepository<Team, HistoryTeam> {
}
