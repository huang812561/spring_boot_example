package com.hgq.rabbitmq.consumer;

import com.hgq.rabbitmq.handler.BaseRabbitMqHandler;
import com.hgq.rabbitmq.handler.MqListener;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

/**
 * 使用通用接口消费消息
 *
 * @Author hgq
 * @Date: 2022-08-30 16:50
 * @since 1.0
 **/
public class TestListener extends BaseRabbitMqHandler<String> {

    @RabbitListener(queues = "orderQueue")
    public void nonTicketUploadHandler(String json, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(json, deliveryTag, channel, new MqListener<String>() { // 实现父类的消息方法里的接口
            @Override
            public void handler(String msg, Channel channel) throws IOException {
                // 业务代码写这里，try/catch不用再写了
                System.out.println("msg = " + msg);
                // 消息消费。。。。。。
            }
        });
    }
}
