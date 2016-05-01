package com.homer.web;

import com.homer.data.PlayerRepository;
import com.homer.data.PlayerSeasonRepository;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.PlayerSeasonService;
import com.homer.service.PlayerService;
import com.homer.service.importer.IPlayerImporter;
import com.homer.service.importer.PlayerImporter;
import com.homer.type.MLBTeam;
import com.homer.type.Player;
import com.homer.type.Team;
import com.homer.util.EnvironmentUtility;
import com.homer.web.flyway.Migrate;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

/**
 * Created by arigolub on 2/15/16.
 */
public class MyApp  {

    public static void main(String[] args) {

        EnvironmentUtility envUtil = EnvironmentUtility.getInstance();
        if (envUtil.shouldMigrate()) {
            Migrate.doWork();
        }

        Scheduler scheduler = new Scheduler();
        scheduler.run();

        String port = envUtil.getInstancePort();
        String uri = "http://0.0.0.0:" + port;
        GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), getResourceConfig());

        System.out.println("Exposing app at: " + uri);
    }

    static ResourceConfig getResourceConfig() {
        ResourceConfig rc = new ResourceConfig().packages("com.homer.web");
        rc.register(JacksonFeature.class);
        return rc;
    }
}
