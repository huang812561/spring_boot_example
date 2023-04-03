package com.hgq.service;


public interface PublisherApi {
    void publishMail(String message);//使用fanout交换机发布消息给所有队列

    void sendDirectMail(String routingkey, String message);//使用direct交换机发送消息

    void sendTopicMail(String routingkey, String message);//使用topic交换机发送消息
}
