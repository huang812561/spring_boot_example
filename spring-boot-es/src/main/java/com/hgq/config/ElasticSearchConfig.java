package com.hgq.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ES配置类
 *
 * @Author hgq
 * @Date: 2022-03-14 14:36
 * @since 1.0
 **/
@Configuration
@ConditionalOnProperty(name = "es.prop.enabled", havingValue = "true")
@ConditionalOnClass(RestClientBuilder.class)
@EnableConfigurationProperties(ElasticProperties.class)
public class ElasticSearchConfig {
    @Autowired
    private ElasticProperties elasticProperties;

    @Bean
    @ConditionalOnMissingBean(RestHighLevelClient.class)
    public RestHighLevelClient restHighLevelClient() {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticProperties.getUserName(),
                        elasticProperties.getPassword()));

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(
                        elasticProperties.getHostUrl(),
                        elasticProperties.getPort()))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                        return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                })
                .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                        return builder.setConnectTimeout(elasticProperties.getConnectTimeout())
                                .setSocketTimeout(elasticProperties.getSocketTimeout())
                                .setMaxRedirects(elasticProperties.getMaxRetryTimes());
                    }
                })
        );
        return client;
    }
}
