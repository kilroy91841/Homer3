package com.homer.service.full;

import com.homer.type.Vulture;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 5/5/16.
 */
public interface IFullVultureService {

    Vulture createVulture(long vultureTeamId, long playerId, @Nullable Long dropPlayerId, boolean isCommissionerVulture);

    Vulture resolveVulture(long id);

    boolean markInProgressVultureForPlayerAsFixed(long playerId);

    List<Vulture> getInProgressVultures();
}
