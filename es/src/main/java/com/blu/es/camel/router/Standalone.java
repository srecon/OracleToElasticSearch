package com.blu.es.camel.router;

import org.apache.camel.CamelContext;
import org.apache.camel.main.Main;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by shamim on 04/08/15.
 */
public class Standalone {
    private Main main;
    public static void main(String[] args){
        Standalone standalone = new Standalone();
        try {
            standalone.boot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void boot() throws Exception{
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
        CamelContext  camelContext = SpringCamelContext.springCamelContext(applicationContext, false);
        main = new Main();
        main.enableHangupSupport();

        camelContext.start();
        main.getOrCreateCamelContext();
        main.run();
    }
}
