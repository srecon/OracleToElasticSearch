package com.blu.db;

import com.blu.es.dto.QRNEvent;
import com.blu.es.dto.TableQrns;
import com.blu.mq.MQConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Lists;
import oracle.jdbc.dcn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.*;
import java.util.List;

/**
 * Created by shamim on 21/07/15.
 */
public class NsiListner implements DatabaseChangeListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(NsiListner.class);
    //private HashMap<Integer, List<ORNEvent>> events = new HashMap<Integer, List<ORNEvent>>();
    private TableQrns tableQrns;
    private ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context.xml");
    private Connection connection;
    private String queueName;

    public NsiListner(String queueName){
        this.queueName = queueName;
    }
    @Override
    public void onDatabaseChangeNotification(DatabaseChangeEvent databaseChangeEvent) {
        for(QueryChangeDescription qcd : databaseChangeEvent.getQueryChangeDescription()){
            LOGGER.info("Query Id: {}", qcd.getQueryId());
            LOGGER.info("Event Type: {}", qcd.getQueryChangeEventType().name());
            Integer tableId = null;
            //tableQrns =  new TableQrns()
            final List<QRNEvent> ornEvents = Lists.newArrayList();
            for(TableChangeDescription tcd : qcd.getTableChangeDescription()){
                //ClassDescriptor descriptor = OracleChangeNotificationListener.this.descriptorsByTable.get(new DatabaseTable(tcd.getTableName()));
                LOGGER.info("table Name: {}", tcd.getTableName()); // table name is empty
                LOGGER.info("Object ID: {}", tcd.getObjectNumber()); // use object id
                tableId = tcd.getObjectNumber();
                for(RowChangeDescription rcd : tcd.getRowChangeDescription()){
                    LOGGER.info("Row ID:" + rcd.getRowid().stringValue() + " Operation:" + rcd.getRowOperation().name());
                    ornEvents.add(new QRNEvent(rcd.getRowOperation().name(), rcd.getRowid().stringValue()));
                }
            }
            if(tableId!=null){
                //events.put(tableId, ornEvents);
                tableQrns = new TableQrns(tableId.intValue(), "", ornEvents);
                // send message
                XmlMapper xmlMapper = new XmlMapper();
                try {
                    String message = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tableQrns);
                    LOGGER.info("Sent Message:"+ message);
                    sendMessage(message);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private MQConnectionFactory getBean(){
        return (MQConnectionFactory) ctx.getBean("mqConnectionFactory");
    };
    private Connection getConnection() throws Exception{
        connection = getBean().getConnectionFactory().createConnection();
        return connection;
    }
    private void sendMessage(String message){
        if(message != null && !message.isEmpty()){
            try {
                getConnection().start();
                Session session =connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue(this.queueName);
                MessageProducer procedure =  session.createProducer(destination);
                TextMessage msg = session.createTextMessage(message);
                procedure.send(msg);
                procedure.close();
                session.close();
                closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void closeConnection(){
       if(connection != null){
           try {
               connection.close();
           } catch (JMSException e) {
               e.printStackTrace();
           }
       }
    }


}
