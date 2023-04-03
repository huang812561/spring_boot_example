package com.hgq.rabbitmq.listener.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class SubscribeListener2 {
    @RabbitListener(queues = "queue2")
    public void subscribe(String mail) throws IOException {
        System.out.println("订阅者2收到消息" + mail);
    }
}
