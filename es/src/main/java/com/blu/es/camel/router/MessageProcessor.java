package com.blu.es.camel.router;

import com.blu.es.dto.QRNEvent;
import com.blu.es.dto.TableObjectsDTO;
import com.blu.es.dto.TableQrns;

import com.blu.es.mapper.TableRowMapper;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by shamim on 07/08/15.
 */
public class MessageProcessor implements Processor{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);
    private static final String SQL_ROWID = "select * from ";

    private static final String SQL_META_DATA="select uo.object_name, ut.column_name from user_tab_columns ut, user_objects uo\n"+
            "  where uo.object_id = ?"+
            "        and uo.OBJECT_NAME = ut.TABLE_NAME";
    // TODO use google guauva
    private static final HashMap<Integer, String> TABLE_NAME_CACHE = new HashMap<Integer, String>();
    private static final HashMap<String, List<String>> TABLE_META_DATA = new HashMap<String, List<String>>();

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
        }else{
            // query the database table for getting table metadata by objectId
            List<TableObjectsDTO> tableObjectsDTO = (List<TableObjectsDTO>)getOraJdbcTemplate().query(SQL_META_DATA, new Object[]{objectId}, new TableRowMapper());
            tableName = tableObjectsDTO.get(0).getTableName();
            TABLE_NAME_CACHE.put(objectId, tableName);
            List<String> colNames = new ArrayList<String>();
            tableObjectsDTO.forEach(
                    p->{
                        colNames.add(p.getColumnName());
                    }
            );
            TABLE_META_DATA.put(tableName, colNames);
        }

        importToES(tableQrns, tableName);
    }
    private void importToES(TableQrns tableQrns, String tableName){
        final String SQL_FOR_ROWID = SQL_ROWID+tableName+" where rowId=?";
        if (tableQrns != null){
            for(QRNEvent event :  tableQrns.getQrnEvent()){
                List<Map<String, Object>> rows =  getOraJdbcTemplate().queryForList(SQL_FOR_ROWID,new Object[]{event.getRowId()});
                for(Map row : rows){
                    for(String colName : TABLE_META_DATA.get(tableName)){
                        System.out.println(row.get(colName));
                    }

                }
            }
        }

    }
}
