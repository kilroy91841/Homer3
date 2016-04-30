package com.homer.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by arigolub on 3/13/16.
 */
public class EnvironmentUtility {

    private String databaseUrl;
    private String databaseUser;
    private String databasePassword;
    private String instanceIP;
    private String instancePort;
    private String databaseSchema;

    private boolean doMigrate;
    private boolean doClean;

    public int update40ManRostersDelay;
    public int update40ManRostersPeriod;
    public int updatePlayersDelay;
    public int updatePlayersPeriod;

    private static String BOXFUSE_DATABASE_URL = "BOXFUSE_DATABASE_URL";
    private static String BOXFUSE_DATABASE_USER = "BOXFUSE_DATABASE_USER";
    private static String BOXFUSE_DATABASE_PASSWORD = "BOXFUSE_DATABASE_PASSWORD";
    private static String BOXFUSE_ENV = "BOXFUSE_ENV";
    private static String BOXFUSE_INSTANCE_IP = "BOXFUSE_INSTANCE_IP";
    private static String BOXFUSE_PORTS_HTTP = "BOXFUSE_PORTS_HTTP";

    private EnvironmentUtility() throws ConfigurationException {
        String boxfuseEnv = System.getProperty(BOXFUSE_ENV);
        PropertiesConfiguration config;
        if (boxfuseEnv != null) {
            config = new PropertiesConfiguration("prop_local.properties");

            setDatabaseUrl(System.getProperty(BOXFUSE_DATABASE_URL));
            setDatabaseUser(System.getProperty(BOXFUSE_DATABASE_USER));
            setDatabasePassword(System.getProperty(BOXFUSE_DATABASE_PASSWORD));
            setInstanceIP(System.getProperty(BOXFUSE_INSTANCE_IP));
            setInstancePort(System.getProperty(BOXFUSE_PORTS_HTTP));
        } else {
            config = new PropertiesConfiguration("prop_local.properties");

            setDatabaseUrl(config.getString("databaseUrl"));
            setDatabaseUser(config.getString("databaseUser"));
            setDatabasePassword(config.getString("databasePassword"));
            setInstanceIP(config.getString("instanceIp"));
            setInstancePort(config.getString("instancePort"));
        }

        setShouldMigrate(config.getBoolean("migrate"));
        setDatabaseSchema(config.getString("schema"));
        setShouldClean(config.getBoolean("clean"));
        setUpdate40ManRostersDelay(config.getInt("update40ManRostersDelay"));
        setUpdate40ManRostersPeriod(config.getInt("update40ManRostersPeriod"));
        setUpdatePlayersDelay(config.getInt("updatePlayersDelay"));
        setUpdatePlayersPeriod(config.getInt("updatePlayersPeriod"));
    }

    private static EnvironmentUtility instance;

    public static EnvironmentUtility getInstance() {
        if (instance == null) {
            try {
                instance = new EnvironmentUtility();
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    private void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    private void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    private void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getInstanceIP() {
        return instanceIP;
    }

    private void setInstanceIP(String instanceIP) {
        this.instanceIP = instanceIP;
    }

    public String getInstancePort() {
        return instancePort;
    }

    private void setInstancePort(String instancePort) {
        this.instancePort = instancePort;
    }

    public boolean shouldMigrate() {
        return doMigrate;
    }

    private void setShouldMigrate(boolean doMigrate) {
        this.doMigrate = doMigrate;
    }

    public String getDatabaseSchema() {
        return databaseSchema;
    }

    private void setDatabaseSchema(String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    public boolean shouldClean() {
        return doClean;
    }

    public void setShouldClean(boolean doClean) {
        this.doClean = doClean;
    }

    public int getUpdate40ManRostersDelay() {
        return update40ManRostersDelay;
    }

    public void setUpdate40ManRostersDelay(int update40ManRostersDelay) {
        this.update40ManRostersDelay = update40ManRostersDelay;
    }

    public int getUpdate40ManRostersPeriod() {
        return update40ManRostersPeriod;
    }

    public void setUpdate40ManRostersPeriod(int update40ManRostersPeriod) {
        this.update40ManRostersPeriod = update40ManRostersPeriod;
    }

    public int getUpdatePlayersDelay() {
        return updatePlayersDelay;
    }

    public void setUpdatePlayersDelay(int updatePlayersDelay) {
        this.updatePlayersDelay = updatePlayersDelay;
    }

    public int getUpdatePlayersPeriod() {
        return updatePlayersPeriod;
    }

    public void setUpdatePlayersPeriod(int updatePlayersPeriod) {
        this.updatePlayersPeriod = updatePlayersPeriod;
    }
}