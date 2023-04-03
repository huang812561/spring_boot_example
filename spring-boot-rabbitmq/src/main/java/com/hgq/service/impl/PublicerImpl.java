package com.hgq.service.impl;

import com.hgq.config.RabbitMqProperties;
import com.hgq.service.PublisherApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-07 15:04
 * @since 1.0
 **/
@Service
public class PublicerImpl implements PublisherApi {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqProperties properties;

    @Override
    public void publishMail(String message) {
        rabbitTemplate.convertAndSend("fanout", "", message);
    }

    @Override
    public void sendDirectMail(String routingkey, String message) {
        routingkey = StringUtils.isNotBlank(routingkey) ? routingkey : properties.getQueueOne();
        rabbitTemplate.convertAndSend("direct", routingkey, message);
    }

    @Override
    public void sendTopicMail(String routingkey, String message) {
        rabbitTemplate.convertAndSend("mytopic", routingkey, message);
    }
}
