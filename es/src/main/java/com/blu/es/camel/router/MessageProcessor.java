package com.blu.es.camel.router;

import com.blu.es.dto.TableQrns;
import com.blu.es.dto.UserObjectDTO;
import com.blu.es.mapper.TableRowMapper;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by shamim on 07/08/15.
 */
public class MessageProcessor implements Processor{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);
    private static final String SQL_METADATA= "select * from user_objects t where t.OBJECT_ID = ?";
    private static final String SQL_ROWID = "select * from ";
    private static final String SQL_TABLE_DESC = "select *\n" +
            "  from user_tab_columns\n" +
            " where table_name = ?";
    private static final HashMap<Integer, String> TABLE_NAME_CACHE = new HashMap<Integer, String>();
    private static final HashMap<String, List<String>> TABLE_COLUMNS = new HashMap<String, List<String>>();

    @Autowired
    private JdbcTemplate oraJdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedOraJdbcTemplate;

    public JdbcTemplate getOraJdbcTemplate() {
        return oraJdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedOraJdbcTemplate() {
        return namedOraJdbcTemplate;
    }

    // Process Every Message
    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.info("Got Message from exchange!!");
        Message msg = exchange.getIn();

        String msgBody = msg.getBody().toString();
        //LOGGER.info("Message:"+ msgBody);
        // deserilized to object - unmarshal
        XmlMapper xmlMapper = new XmlMapper();
        TableQrns tableQrns = xmlMapper.readValue(msgBody, TableQrns.class);
        final int objectId = tableQrns.getObjectId();
        LOGGER.info("ObectId {}", objectId);
        String tableName="";
        if(!TABLE_NAME_CACHE.isEmpty() && TABLE_NAME_CACHE.containsKey(Integer.valueOf(objectId))){
            tableName = TABLE_NAME_CACHE.get(Integer.valueOf(objectId));
            // get table description

        }else{
            // query the database table for getting table name by objectId
            UserObjectDTO userObject = (UserObjectDTO) getOraJdbcTemplate().queryForObject(SQL_METADATA, new Object[]{objectId}, new TableRowMapper());
            tableName = userObject.getObjectName();
            TABLE_NAME_CACHE.put(Integer.valueOf(objectId), tableName);
            // get table description
            getOraJdbcTemplate().query(SQL_TABLE_DESC, new Object[]{tableName}, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet resultSet) throws SQLException {
                    LOGGER.info("Column Name:"+ resultSet.getString(1)+ " DataType:"+ resultSet.getString(2));
                    TABLE_COLUMNS.put("TEMP", new ArrayList<String>(){{resultSet.getString(1);}});
                }
            });

        }
        List<String> columns  =  TABLE_COLUMNS.get("TEMP");
        for(String column : columns){
            LOGGER.info("COLUMN NAME:" + column);
        }

        // TODO should be prepared statement
        //final String SQL_FOR_ROWID = SQL_ROWID+tableName+" where rowId=?";

    }
}
