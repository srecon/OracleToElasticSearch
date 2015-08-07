package com.blu.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by shamim on 21/07/15.
 */
public class ContextRunner {
    public static void main(String[] args) throws Exception{
        System.out.println("Run Spring context!!");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context.xml");
        DBNotifactionConsumer consumer= (DBNotifactionConsumer) ctx.getBean("consumer");
        consumer.registerNotification();
    }
}
