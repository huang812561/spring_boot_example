package com.hgq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-03-14 14:46
 * @since 1.0
 **/
@Data
@ConfigurationProperties("es.prop")
public class ElasticProperties {
    private String userName = "elastic";
    private String password;
    private String hostUrl;
    private int port = 9200;
    private int connectTimeout = 5 * 1000;
    private int socketTimeout = 60 * 1000;
    private int maxRetryTimes = 3;
}
