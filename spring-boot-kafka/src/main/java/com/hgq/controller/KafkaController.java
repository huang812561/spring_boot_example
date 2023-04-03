package com.hgq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-04-06 15:19
 * @since 1.0
 **/
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${hgq.kafka.topic}")
    private String topic;

    @GetMapping("/send/{message}")
    public String send(@PathVariable(name = "message") String message) {
        kafkaTemplate.send(topic, message);
        //kafkaTemplate.send(topic,"key", message);
        return "发送成功";
    }
}
