package com.hgq.rabbitmq.listener.dicrect;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Direct Exchange  : direct
 * Queue            : directqueue2
 * RoutingKey       : base
 *
 * @Author hgq
 * @Date: 2022-07-07 15:30
 * @since 1.0
 **/
@Component
public class DirectListener2 {

    @RabbitListener(queues = "directqueue2")
    public void receiveMessage(String message) throws IOException {
        System.out.println("directqueue2队列监听器2号收到消息：" + message);
    }
}
