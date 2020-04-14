package cn.qd.search.cfg;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Value("${beibei.elasticsearch.host}")
    private String host;

    @Value("${beibei.elasticsearch.port}")
    private Integer port;

    @Bean
    public RestHighLevelClient getRestHighLevelClient(){
        HttpHost httpHost = new HttpHost(host, port, "http");
        return  new RestHighLevelClient(RestClient.builder(httpHost));
    }

}
