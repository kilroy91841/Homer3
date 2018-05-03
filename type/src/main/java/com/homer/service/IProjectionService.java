package com.homer.service;

import com.homer.type.Projection;

import java.util.List;

/**
 * Created by arigolub on 5/2/18.
 */
public interface IProjectionService extends IIdService<Projection> {
    List<Projection> saveProjections();
}
