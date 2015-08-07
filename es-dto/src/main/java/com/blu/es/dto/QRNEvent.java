package com.blu.es.dto;

//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Created by shamim on 06/08/15.
 */
//@JacksonXmlRootElement(localName = "qrnEvent")
public class QRNEvent {
    /*
    * Operation name, update, insert, delete
    * */
    private String operationName;
    /*
    * rowid
    * */
    private String rowId;

    public QRNEvent() {  }

    public QRNEvent(String operationName, String rowId) {
        this.operationName = operationName;
        this.rowId = rowId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getRowId() {
        return rowId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QRNEvent qrnEvent = (QRNEvent) o;

        if (operationName != null ? !operationName.equals(qrnEvent.operationName) : qrnEvent.operationName != null)
            return false;
        if (!rowId.equals(qrnEvent.rowId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operationName != null ? operationName.hashCode() : 0;
        result = 31 * result + rowId.hashCode();
        return result;
    }
}
