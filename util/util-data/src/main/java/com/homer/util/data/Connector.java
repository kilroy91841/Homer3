package com.homer.util.data;

import com.homer.util.EnvironmentUtility;
import com.homer.util.core.IBaseObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

/**
 * Created by arigolub on 2/14/16.
 */
public class Connector {

    public static Connection getConnection() throws SQLException {
        EnvironmentUtility envUtil = EnvironmentUtility.getInstance();
        Properties connectionProps = new Properties();
        connectionProps.put("user", envUtil.getDatabaseUser());
        connectionProps.put("password", envUtil.getDatabasePassword());

        Connection conn = DriverManager.getConnection(envUtil.getDatabaseUrl(), connectionProps);
        conn.setAutoCommit(false);
        System.out.println("Connected to database");
        return conn;
    }
}
