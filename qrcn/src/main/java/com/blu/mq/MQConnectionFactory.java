package com.blu.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.*;

/**
 * Created by shamim on 06/08/15.
 */
public class MQConnectionFactory {
    private String userName;
    private String password;
    private String broker;
    private static ActiveMQConnectionFactory connectionFactory;

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

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }
    public ConnectionFactory getConnectionFactory(){
        if(connectionFactory == null ){
            connectionFactory = new ActiveMQConnectionFactory(this.userName, this.password, this.broker);
            PooledConnectionFactory pooledConnectionFactory = new org.apache.activemq.pool.PooledConnectionFactory(connectionFactory);
            pooledConnectionFactory.setMaxConnections(10);
            return pooledConnectionFactory;
        }


        return connectionFactory;
    }

}
