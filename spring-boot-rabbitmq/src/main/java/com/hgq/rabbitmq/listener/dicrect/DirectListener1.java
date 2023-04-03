package com.hgq.rabbitmq.listener.dicrect;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Direct Exchange  : direct
 * Queue            : directqueue1
 * RoutingKey       : order
 *
 * @Author hgq
 * @Date: 2022-07-07 15:30
 * @since 1.0
 **/
@Component
public class DirectListener1 {

    @RabbitListener(queues = "directqueue1")
    public void receiveMessage(String message) throws IOException {
        System.out.println("directqueue1队列监听器1号收到消息："+message);
    }
}
