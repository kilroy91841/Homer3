package com.homer.data;

import com.homer.data.common.ITeamRepository;
import com.homer.type.Team;
import com.homer.type.history.HistoryTeam;
import com.homer.util.data.BaseRepository;
import com.homer.util.data.BaseVersionedRepository;

/**
 * Created by arigolub on 3/15/16.
 */
public class TeamRepository extends BaseVersionedRepository<Team, HistoryTeam> implements ITeamRepository {

    public TeamRepository() {
        super(Team.class, HistoryTeam.class);
    }
}
