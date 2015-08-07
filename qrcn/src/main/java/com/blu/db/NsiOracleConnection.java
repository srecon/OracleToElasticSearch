package com.blu.db;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.driver.OracleDriver;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by shamim on 21/07/15.
 */
public class NsiOracleConnection {
    private String userName;
    private String password;
    private String connectionString;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
    public OracleConnection getConnection() throws SQLException{
        final OracleDriver oracleDriver = new OracleDriver();
        Properties oracleProperties = new Properties();
        oracleProperties.put("user", getUserName());
        oracleProperties.put("password", getPassword());

        return (OracleConnection)oracleDriver.connect(getConnectionString(), oracleProperties);
    }

}
