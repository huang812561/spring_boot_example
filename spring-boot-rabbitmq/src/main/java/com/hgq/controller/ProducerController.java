package com.hgq.controller;

import com.hgq.service.ProducerApi;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-07 17:23
 * @since 1.0
 **/
@RestController
public class ProducerController {

    @Autowired
    private ProducerApi producerApi;
    
    @RequestMapping("/produce/{message}/{count}")
    public String produce(@PathVariable("message") String message, @PathVariable("count") int count) {
        for (int i = 1; i <= count; i++) {
            producerApi.sendMessage("myQueue", message);
        }
        System.out.println("###########消息发送成功##################");
        return "消息发送成功";
    }
}
