package com.blu.db;

import com.blu.es.dto.QRNEvent;
import com.blu.es.dto.TableQrns;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by shamim on 06/08/15.
 */
// TODO replace in package es-dto
public class XMLBindingTest {
    @Test
    public void testBinding(){
        XmlMapper xmlMapper = new XmlMapper();
        try {
            //String xml = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ORNEvent("operation name","rowid"));
            TableQrns tableQrns = new TableQrns(1, "tablename", new ArrayList<QRNEvent>(){{add(new QRNEvent("a","b")); add(new QRNEvent("c","d"));
                    }} );

            String xml = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tableQrns);
            Assert.assertNotNull(xml);
            System.out.println(xml);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testDeserialization() throws Exception{
        ObjectMapper objectMapper = new XmlMapper();
        TableQrns tableQrns = objectMapper.readValue("<TableQrns>\n" +
                "  <objectId>11</objectId>\n" +
                "  <tableName>tablenameTest</tableName>\n" +
                "  <qrnEvents>\n" +
                "    <qrnEvent>\n" +
                "      <operationName>a1</operationName>\n" +
                "      <rowId>b1</rowId>\n" +
                "    </qrnEvent>\n" +
                "    <qrnEvent>\n" +
                "      <operationName>c1</operationName>\n" +
                "      <rowId>d1</rowId>\n" +
                "    </qrnEvent>\n" +
                "  </qrnEvents>\n" +
                "</TableQrns>", TableQrns.class);
        Assert.assertNotNull(tableQrns.getObjectId());
        Assert.assertEquals("array size:",2, tableQrns.getQrnEvent().size());
    }
}
