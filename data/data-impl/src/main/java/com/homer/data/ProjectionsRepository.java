package com.homer.data;

import com.homer.type.Projection;
import com.homer.util.data.BaseRepository;

/**
 * @author ari@mark43.com
 * @since 5/2/18
 */
public class ProjectionsRepository extends BaseRepository<Projection>
{
    public ProjectionsRepository()
    {
        super(Projection.class);
    }
}
