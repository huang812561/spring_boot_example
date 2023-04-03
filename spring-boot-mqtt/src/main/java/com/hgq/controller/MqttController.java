package com.hgq.controller;

import com.hgq.config.MqttConsumerProperties;
import com.hgq.config.MqttProducerProperties;
import com.hgq.service.MqttGateway;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-03-29 17:24
 * @since 1.0
 **/
@Slf4j
@RestController
public class MqttController {

    @Autowired
    private MqttProducerProperties properties;
    @Autowired
    private MqttConsumerProperties consumerProperties;


/*    @Autowired
    private MqttClient mqttClient;*/

    @Resource
    private MqttGateway mqttGateway;

    /**
     * 测试
     *
     * @param msg
     * @return
     * @throws MqttException
     */
    @RequestMapping("/send")
    public String send(@RequestParam("msg") String msg) throws MqttException {
        //mqttClient.publish(properties.getTopic(), msg.getBytes(StandardCharsets.UTF_8), 1, false);
        mqttGateway.sendToMqttMsg(properties.getTopic(), msg);
        return "主题：" + properties.getTopic() + "\r\n发送内容：【" + msg + " 】成功";
    }


    /**
     * 测试
     *
     * @param msg
     * @return
     * @throws MqttException
     */
    @RequestMapping("/send2")
    public String send2(@RequestParam("msg") String msg) throws MqttException {
        mqttGateway.sendToMqtt(msg);
        return "主题：" + consumerProperties.getConsumerTopics() + "\r\n发送内容：【" + msg + " 】成功";
    }

}
