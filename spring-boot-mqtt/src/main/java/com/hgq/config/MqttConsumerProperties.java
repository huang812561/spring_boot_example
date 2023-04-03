package com.hgq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * mqtt 消费端 属性文件
 *
 * @Author hgq
 * @Date: 2022-03-29 17:08
 * @since 1.0
 **/
@Data
@Configuration
public class MqttConsumerProperties {

    @Value("${spring.mqtt.consumer.username}")
    private String userName;

    @Value("${spring.mqtt.consumer.password}")
    private String password;

    @Value("${spring.mqtt.consumer.url}")
    private String url;

    /**
     * 消费者的clientId
     */
    @Value("${spring.mqtt.consumer.clientId}")
    private String consumerClientId;

    /**
     * 消费者 订阅的topics
     */
    @Value("${spring.mqtt.consumer.topic}")
    private String consumerTopics;

}
