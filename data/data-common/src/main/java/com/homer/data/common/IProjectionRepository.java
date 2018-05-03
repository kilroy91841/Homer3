package com.homer.data.common;

import com.homer.type.Projection;
import com.homer.util.core.data.IRepository;

import java.util.List;

/**
 * Created by arigolub on 5/2/18.
 */
public interface IProjectionRepository extends IRepository<Projection>
{
    List<Projection> getForSeason(int season);
}
