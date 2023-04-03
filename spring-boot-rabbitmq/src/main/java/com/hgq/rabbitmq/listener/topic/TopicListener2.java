package com.hgq.rabbitmq.listener.topic;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-07 15:33
 * @since 1.0
 **/
@Component
public class TopicListener2 {

    @RabbitListener(queues = "topicqueue2")
    public void topicMessage(String message) throws IOException {
        System.out.println("从topicqueue2取出消息：" + message);
    }
}
