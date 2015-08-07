package com.blu.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import oracle.jdbc.dcn.*;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleDriver;
import oracle.jdbc.OracleStatement;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * Created by shamim on 30/05/15.
 */
// @TODO delete soon
public class DbEventConsumer {
    private static final String USERNAME="dontdelete1";
    private static final String PASSWORD = "dontdelete1";
    private static final String URL="jdbc:oracle:thin:@sbrf-backbase-db.vps.at-consulting.ru:1521:DB11G";

    public static void main(String[] args) throws Exception{
        //System.out.println("Event Consumer Start...");
        DbEventConsumer consumer = new DbEventConsumer();
        consumer.run();
    }
    private void run() throws Exception {
        System.out.println("Event Consumer Start...");
        OracleConnection connection = getConnection();
        Properties properties = new Properties();
        properties.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
        properties.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION, "true"); //Activates query change notification instead of object change notification.

        // Register notifier
        DatabaseChangeRegistration reg = connection.registerDatabaseChangeNotification(properties);

        reg.addListener(new DatabaseChangeListener() {
            @Override
            public void onDatabaseChangeNotification(DatabaseChangeEvent databaseChangeEvent) {
                //TableChangeDescription tcd =  databaseChangeEvent.getTableChangeDescription()[0];
                QueryChangeDescription qcd =  databaseChangeEvent.getQueryChangeDescription()[0];
                TableChangeDescription tcd = qcd.getTableChangeDescription()[0];
                for(RowChangeDescription rcd : tcd.getRowChangeDescription()){
                    System.out.println("Updated Row ID:" + rcd.getRowid().stringValue() + " Operation:" + rcd.getRowOperation().name());
                    //System.out.printf("Full event: " + databaseChangeEvent);
                /*// emit rowid to Kafka cluster
                    ProducerRecord<String,String> record = new ProducerRecord<String,String>(Producer.KAFKA_TOPIC, rcd.getRowid().stringValue(), rcd.getRowid().stringValue());

                    Producer.getKafkaProducer().send(record, new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            if(e != null){
                                e.printStackTrace();
                            } else{
                                System.out.printf("Message sent!");
                            }
                        }
                    });*/
                }
            }
        });

        String query = "select * from temp t where t.a = 'a1'";
        Statement stm = connection.createStatement();
        ((OracleStatement) stm).setDatabaseChangeRegistration(reg);

        ResultSet rs = stm.executeQuery(query);
        //stm.executeQuery("select 1 from fdc_vt where 1!=2");
        //stm.executeQuery("select 1 from fdc_pt where 1!=2");
        while(rs.next()){
        }
        // get tables from dcr
        String[] tables = reg.getTables();
        for(String str : tables){
            System.out.println("Tables:"+ str);
        }
        rs.close();
        stm.close();
    }


    private OracleConnection getConnection() throws SQLException {
        OracleDriver driver = new OracleDriver();
        Properties prop = new Properties();
        prop.setProperty("user",this.USERNAME);
        prop.setProperty("password",this.PASSWORD);
        return (OracleConnection)driver.connect(this.URL,prop);
    }

    public static class Producer {
        private static final String KAFKA_HOST = "localhost";
        private static final String KAFKA_PORT = "9092";
        private static final String KAFKA_TOPIC = "test";
        private static HashMap<String, Object> config = new HashMap<String, Object>();

        private static KafkaProducer kafkaProducer;
        static{
            config.put("producer.type", "sync");
            config.put("serializer.class", "kafka.serializer.StringEncoder");
            config.put("bootstrap.servers", KAFKA_HOST+":"+KAFKA_PORT);
            config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            config.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
            config.put("request.required.acks", "1");
            kafkaProducer = new KafkaProducer(config);
        }

        public static KafkaProducer getKafkaProducer(){
            if(kafkaProducer == null){
                return new KafkaProducer(config);
            }
            return kafkaProducer;
        }

    }
}
