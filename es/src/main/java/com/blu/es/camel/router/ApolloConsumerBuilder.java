package com.blu.es.camel.router;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shamim on 04/08/15.
 */
public class ApolloConsumerBuilder extends RouteBuilder{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApolloConsumerBuilder.class);
    // TODO add connection pool in Spring context
    @Override
    public void configure() throws Exception {
        LOGGER.info("MaPreviewBuilder start!!");
/*        from("timer://timer1?period=1000")
                .process(new Processor() {
                    public void process(Exchange msg) {
                        LOGGER.info("Processing {}", msg);
                    }
                });*/
        //send message to queue test every second
/*
        from("timer:foo?period=1s")
                .transform()
                .simple("Heartbeat ${date:now:yyyy-MM-dd HH:mm:ss}").to("stomp:queue:test?brokerURL=tcp://192.168.24.72:61613&login=admin&passcode=password");
*/

        //uri="activemq:foo?destination.consumer.exclusive=true&amp;destination.consumer.prefetchSize=50
        // get message from queue Test
/*        from("stomp:queue:test?brokerURL=tcp://192.168.24.72:61613&login=admin&passcode=password")
            .log(LoggingLevel.INFO, "got message-${header.CamelFileNameOnly}")
            .beanRef("messageProcessor","process")
                    //.transform(body().convertTo(org.w3c.dom.Document.class))
            .to("mock:result");
            //.to("file:/Volumes/Macintosh HD/Users/shamim/temp/data?fileName=transfer${in.header.CamelSplitIndex}.txt");*/
        from("activemq:test")
        .log(LoggingLevel.INFO, "got message-${header.CamelFileNameOnly}")
        .beanRef("messageProcessor", "process")
        //.transform(body().convertToString())
        .to("mock:result");
    }
}
