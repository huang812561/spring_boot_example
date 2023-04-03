package com.hgq.rabbitmq.listener.topic;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-07 15:33
 * @since 1.0
 **/
@Component
public class TopicListener1 {

    @RabbitListener(queues = "topicqueue1")
    public void topicMessage(String message){
        System.out.println("从topicqueue1取出消息："+message);
    }
}
