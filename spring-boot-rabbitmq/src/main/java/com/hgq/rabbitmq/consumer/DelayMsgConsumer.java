package com.hgq.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 延时消息消费
 *
 * @Author hgq
 * @Date: 2022-08-30 19:05
 * @since 1.0
 **/
@Component
public class DelayMsgConsumer {

    @RabbitListener(queuesToDeclare = @Queue(value = "delayTest", durable = "true"))
    public void declareExchange(String message) {
        System.out.println("delayTest = " + message);

    }


}
