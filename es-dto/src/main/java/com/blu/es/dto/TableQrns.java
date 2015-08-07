package com.blu.es.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

/**
 * Created by shamim on 06/08/15.
 */
public class TableQrns {

    private int objectId;
    private String tableName;
    @JacksonXmlElementWrapper(useWrapping = true, localName = "qrnEvents")
    private List<QRNEvent> qrnEvent;

    public TableQrns(int objectId, String tableName, List<QRNEvent> qrnEvent) {
        this.objectId = objectId;
        this.tableName = tableName;
        this.qrnEvent = qrnEvent;
    }

    public TableQrns() {  }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<QRNEvent> getQrnEvent() {
        return qrnEvent;
    }

    public void setQrnEvent(List<QRNEvent> qrnEvent) {
        this.qrnEvent = qrnEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableQrns tableQrns = (TableQrns) o;

        if (objectId != tableQrns.objectId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return objectId;
    }
}
