package com.blu.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shamim on 24/07/15.
 */
public class ExecutesSecondsClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteClients.class);
    private static final String ZOOKEEPER_URI="127.0.0.1:2181";
    private static final String PATH="/zk_test";

    public static void main(String[] args) throws Exception{
        LOGGER.info("Starting client");
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(500, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZOOKEEPER_URI, retryPolicy);
        client.start();
        // second one
        SimpleClient secondClient = new SimpleClient("second", client, PATH);
        secondClient.start();
        Thread.sleep(Long.MAX_VALUE);
    }
}
