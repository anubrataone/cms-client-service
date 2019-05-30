package com.elasticsearch.cms.application;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("search.engine")
public class SearchEngineConfig {

    private String esHost;
    private int esPort;
    private String esClusterName;

    public String getEsHost() {
        return esHost;
    }

    public void setEsHost(String esHost) {
        this.esHost = esHost;
    }

    public int getEsPort() {
        return esPort;
    }

    public void setEsPort(int esPort) {
        this.esPort = esPort;
    }

    public String getEsClusterName() {
        return esClusterName;
    }

    public void setEsClusterName(String esClusterName) {
        this.esClusterName = esClusterName;
    }

    @Bean(destroyMethod = "close")
    public RestHighLevelClient getTransportClient() {
        Settings.Builder settings = Settings.builder();
        settings.put("node.client", true);
        settings.put("node.data", false);
        settings.put("node.name", "node-client");
        settings.put("cluster.name", "elasticsearch");
        settings.build();

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(getEsHost(), getEsPort(), "http")));

        return client;
    }

}
