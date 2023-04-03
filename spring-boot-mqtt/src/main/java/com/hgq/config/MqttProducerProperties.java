package com.hgq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * mqtt属性文件
 *
 * @Author hgq
 * @Date: 2022-03-29 17:08
 * @since 1.0
 **/
@Data
@Configuration
public class MqttProducerProperties {

    @Value("${spring.mqtt.username}")
    private String userName;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String url;

    @Value("${spring.mqtt.topic}")
    private String topic;

    /**
     * 生产者的clientId
     */
    @Value("${spring.mqtt.client.id}")
    private String clientId;

}
