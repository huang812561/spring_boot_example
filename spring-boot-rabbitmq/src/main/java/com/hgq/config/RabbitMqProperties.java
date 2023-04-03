package com.hgq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-07 14:21
 * @since 1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "rabbit.mq")
public class RabbitMqProperties {
    private String exchangeName;

    private String queueOne;

    private String queueTwo;

    private String queueThree;
}
