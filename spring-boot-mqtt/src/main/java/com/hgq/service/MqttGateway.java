package com.hgq.service;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * 消息推送接口类
 * MessagingGateway绑定MQTT实现发布
 * 注：在发布订阅时，必须加@Header(MqttHeaders.TOPIC)注解
 *
 * @Author hgq
 * @Date: 2022-03-30 10:10
 * @since 1.0
 **/
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    void sendToMqttMsg(@Header(MqttHeaders.TOPIC) String topic, String data);

    void sendToMqtt(String data);

    void sendToMqtt(@Header(MqttHeaders.TOPIC)String topic, int qos, String data);

}
