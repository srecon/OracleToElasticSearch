package com.blu.es.sandbox;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Created by shamim on 08/08/15.
 */
public class ESClient {
    private String host;
    private int port;
    private static Client esClient;

    public ESClient(String host, int port){
        this.host = host;
        this.port = port;
        esClient = new TransportClient().addTransportAddress(new InetSocketTransportAddress(host, port));
    }

    public Client getEsClient(){

        return esClient;
    }
    public void close(){
        if(esClient != null){
            esClient.close();
        }
    }
}
