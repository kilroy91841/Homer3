package com.homer.service;

import com.homer.web.ServiceFactory;
import com.homer.web.flyway.Migrate;

/**
 * Created by arigolub on 5/2/18.
 */
public class ProjectionTest {

    public static void main(String[] args)
    {
        Migrate.doWork();
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        IProjectionService projectionService = serviceFactory.get(IProjectionService.class);
        projectionService.saveProjections();
    }
}
