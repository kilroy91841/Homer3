package com.homer.external.common.mlb;

/**
 * @author ari@mark43.com
 * @since 5/2/18
 */
public class Team
{
    private int teamId;
    private int wins;
    private int losses;

    public int getTeamId()
    {
        return teamId;
    }

    public void setTeamId(int teamId)
    {
        this.teamId = teamId;
    }

    public int getWins()
    {
        return wins;
    }

    public void setWins(int wins)
    {
        this.wins = wins;
    }

    public int getLosses()
    {
        return losses;
    }

    public void setLosses(int losses)
    {
        this.losses = losses;
    }
}
