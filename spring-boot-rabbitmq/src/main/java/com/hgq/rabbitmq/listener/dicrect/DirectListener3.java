package com.hgq.rabbitmq.listener.dicrect;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Direct Exchange  : direct
 * Queue            : directqueue3
 * RoutingKey       : charge
 *
 * @Author hgq
 * @Date: 2022-07-07 15:30
 * @since 1.0
 **/
@Component
public class DirectListener3 {

    @RabbitListener(queues = "directqueue3")
    public void receiveMessage(String message) throws IOException {
        System.out.println("directqueue3队列监听器3号收到消息：" + message);
    }
}
