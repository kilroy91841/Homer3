package com.homer.type;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author ari@mark43.com
 * @since 5/2/18
 */
@Table(name = "projections", schema = "homer")
public class Projection extends BaseDaily
{
    @Column(updatable = false)
    private long playerId;

    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        Projection that = (Projection) o;
        return playerId == that.playerId;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), playerId);
    }

    // endregion equals/hashCode/toString

    // region getters + setters

    public long getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(long playerId)
    {
        this.playerId = playerId;
    }

    // endregion getters + setters
}
