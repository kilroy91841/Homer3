package com.homer.data;

import com.google.common.collect.Maps;
import com.homer.data.common.IProjectionRepository;
import com.homer.type.Projection;
import com.homer.util.data.BaseRepository;

import java.util.List;
import java.util.Map;

/**
 * @author ari@mark43.com
 * @since 5/2/18
 */
public class ProjectionsRepository extends BaseRepository<Projection> implements IProjectionRepository
{
    public ProjectionsRepository()
    {
        super(Projection.class);
    }

    @Override
    public List<Projection> getForSeason(int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("date", String.valueOf(season));
        return getMany(filters);
    }
}
