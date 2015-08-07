package com.blu.curator;

import com.blu.db.DBNotifactionConsumer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shamim on 24/07/15.
 */
public class SimpleClient extends LeaderSelectorListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClient.class);
    private String clientName;
    private CuratorFramework client;
    private String path;
    private LeaderSelector leaderSelector;
    //private final AtomicInteger leaderCount = new AtomicInteger();
    // oracle change notification
    private ApplicationContext ctx;// = new ClassPathXmlApplicationContext("spring-context.xml");
    private DBNotifactionConsumer consumer;//= (DBNotifactionConsumer) ctx.getBean("consumer");

    public SimpleClient(String clientName, CuratorFramework client, String path) {
        this.clientName = clientName;
        this.client = client;
        this.path = path;
        leaderSelector = new LeaderSelector(this.client,this.path, this);
        leaderSelector.autoRequeue();
        // initialize oracle change notification
        ctx = new ClassPathXmlApplicationContext("spring-context.xml");
        consumer = (DBNotifactionConsumer)ctx.getBean("consumer");
    }

    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        // run oracle notification here
        consumer.registerNotification();
        System.out.println(this.clientName + " is now the leader!!");
        Thread.sleep(Long.MAX_VALUE);
    }
    public void start(){
        leaderSelector.start();
    }

}
