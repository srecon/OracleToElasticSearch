package com.blu.es;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by shamim on 04/08/15.
 */
public class CamelBuilderTest {
    @Test
    public void testMapreviewBuilder(){
        System.out.println("Start Test for Consumer!!");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
        //applicationContext.getBean()
        CamelContext camelContext = null;
        try {
            camelContext = SpringCamelContext.springCamelContext(applicationContext, false);
            Thread.sleep(1000*120);
            camelContext.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                camelContext.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
