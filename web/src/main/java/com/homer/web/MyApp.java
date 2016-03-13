package com.homer.web;

import com.homer.util.EnvironmentUtility;
import org.apache.commons.configuration.ConfigurationException;
import org.flywaydb.core.Flyway;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

/**
 * Created by arigolub on 2/15/16.
 */
public class MyApp  {

    public static void main(String[] args) throws ConfigurationException {
        EnvironmentUtility envUtil = EnvironmentUtility.getInstance();

        if (envUtil.shouldMigrate()) {
            Flyway flyway = new Flyway();
            flyway.setDataSource(
                    envUtil.getDatabaseUrl(),
                    envUtil.getDatabaseUser(),
                    envUtil.getDatabasePassword());
            flyway.setSchemas("homer3");
            flyway.migrate();
        }

        // Base URI the Grizzly HTTP server will listen on
        String instanceIP = envUtil.getInstanceIP();
        String port = envUtil.getInstancePort();
        String BASE_URI = "http://" + instanceIP + ":" + port;

        // create a resource config that scans for JAX-RS resources and providers
        // in com.example.rest package
        final ResourceConfig rc = new ResourceConfig().packages("com.homer.web");
        rc.register(JacksonFeature.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        System.out.println(BASE_URI);
        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

    }
}
