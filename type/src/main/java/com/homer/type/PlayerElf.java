package com.homer.type;

import javax.annotation.Nullable;

/**
 * @author ari@mark43.com
 * @since 3/23/18
 */
public final class PlayerElf
{
    private PlayerElf() {}

    public static void switchTeam(PlayerSeason existing, @Nullable Long newTeamId) {
        existing.setTeamId(newTeamId);

        //Free agents should have fantasy position removed and vulturable status set to false
        if (newTeamId == null) {
            existing.setFantasyPosition(null);
            existing.setVulturable(false);
        }
    }

    public static void switchFantasyPosition(PlayerSeason existing, @Nullable Position oldFantasyPosition, @Nullable Position newFantasyPosition)
    {
        if (oldFantasyPosition != existing.getFantasyPosition())
        {
            throw new IllegalArgumentException("Supplied old position does not match existing position");
        }
        existing.setFantasyPosition(newFantasyPosition);

        //Remove minorLeaguer status if player is being added to any position other than Position.MINORLEAGUES
        if (!Position.MINORLEAGUES.equals(newFantasyPosition))
        {
            existing.setIsMinorLeaguer(false);
        }

        updateVulturable(existing);
    }

    public static void updateVulturable(PlayerSeason playerSeason)
    {
        Boolean isVulturable = PlayerElf.isPlayerVulturable(playerSeason);
        if (isVulturable != null) {
            playerSeason.setVulturable(isVulturable);
        }
    }

    private static Boolean isPlayerVulturable(PlayerSeason playerSeason)
    {
        if (playerSeason.getMlbStatus() == Status.UNKNOWN)
        {
            return null;
        }

        Position fantasyPosition = playerSeason.getFantasyPosition();
        if (playerSeason.getIsMinorLeaguer())
        {
            return false;
        }

        if ((fantasyPosition != Position.DISABLEDLIST && playerSeason.getMlbStatus() == Status.DISABLEDLIST) ||
            (fantasyPosition != Position.MINORLEAGUES && playerSeason.getMlbStatus() == Status.MINORS) ||
            (fantasyPosition == Position.DISABLEDLIST && playerSeason.getMlbStatus() != Status.DISABLEDLIST) ||
            (fantasyPosition == Position.MINORLEAGUES && playerSeason.getMlbStatus() != Status.MINORS && !playerSeason.getIsMinorLeaguer()))
        {
            return true;
        }
        return false;
    }
}
