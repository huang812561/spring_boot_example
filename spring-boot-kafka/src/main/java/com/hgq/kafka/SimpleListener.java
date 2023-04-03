package com.hgq.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-04-06 15:18
 * @since 1.0
 **/
@Component
public class SimpleListener {

    @KafkaListener(groupId = "kafka-server-group",idIsGroup = false,topics = {"hgq_topic_test"})
    public void listen(String data) {
        System.out.println("接受到kafka数据：" + data);
    }
}
