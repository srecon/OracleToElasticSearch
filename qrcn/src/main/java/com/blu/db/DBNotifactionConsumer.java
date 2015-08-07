package com.blu.db;

import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.*;
import oracle.jdbc.driver.OracleConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by shamim on 21/07/15.
 */
public class DBNotifactionConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBNotifactionConsumer.class);

    private NsiOracleConnection oracleConnection;
    private static final Properties properties = new Properties();
    private String queryString;
    private String queueName;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    static{
        properties.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
        properties.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION, "true"); //Activates query change notification instead of object change notification.
    }

    public DBNotifactionConsumer(NsiOracleConnection oracleConnection) {
        this.oracleConnection = oracleConnection;
    }

    public NsiOracleConnection getOracleConnection() {
        return oracleConnection;
    }

    public void registerNotification() throws SQLException{
        DatabaseChangeRegistration databaseChangeRegistration =  getOracleConnection().getConnection().registerDatabaseChangeNotification(properties);
        databaseChangeRegistration.addListener(new NsiListner(getQueueName()));
        Statement stm = getOracleConnection().getConnection().createStatement();
        ((OracleStatement) stm).setDatabaseChangeRegistration(databaseChangeRegistration);
        ResultSet rs;
        for(String queryString : getQueryString().split(";")){
            rs = stm.executeQuery(queryString);
            while(rs.next()){
            }
            rs.close();
        }
        // get tables from dcr
        String[] tables = databaseChangeRegistration.getTables();
        for(String str : tables){
            LOGGER.info("Registreted Tables:{}", str);
        }
        if(!stm.isClosed()){
            stm.close();
        }
    }
/*    public DatabaseChangeRegistration getDataBaseChangeNotification() throws SQLException{
        return this.oracleConnection.getConnection().getDatabaseChangeRegistration();
    }*/

}
