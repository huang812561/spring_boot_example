package com.hgq.service.impl;

import com.hgq.service.ProducerApi;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-07 15:01
 * @since 1.0
 **/
@Service
public class ProducerImpl implements ProducerApi {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(String queue, String message) {
        rabbitTemplate.setDefaultReceiveQueue(queue);
        rabbitTemplate.convertAndSend(queue, message);
    }
}
