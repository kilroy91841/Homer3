package com.homer.web.flyway;

import com.homer.util.EnvironmentUtility;

import static com.homer.util.EnvironmentUtility.getInstance;

/**
 * Created by arigolub on 3/13/16.
 */
public class Migrate {

    private static EnvironmentUtility ENV_UTIL = getInstance();

    public static void main(String[] args) {
        doWork();
    }

    public static void doWork() {
        org.flywaydb.core.Flyway flyway = new org.flywaydb.core.Flyway();
        flyway.setDataSource(
                ENV_UTIL.getDatabaseUrl(),
                ENV_UTIL.getDatabaseUser(),
                ENV_UTIL.getDatabasePassword());
        flyway.setSchemas(ENV_UTIL.getDatabaseSchema());
        flyway.repair();
        if (ENV_UTIL.shouldClean()) {
            flyway.clean();
        }
        flyway.migrate();
    }
}
