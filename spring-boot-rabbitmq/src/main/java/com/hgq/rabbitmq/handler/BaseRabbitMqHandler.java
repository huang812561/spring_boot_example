package com.hgq.rabbitmq.handler;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-08-30 16:45
 * @since 1.0
 **/
@Slf4j
public class BaseRabbitMqHandler<T> {
    public void onMessage(T t, long deliveryTag, Channel channel, MqListener mqListener){

        try {
            mqListener.handler(t,channel);

            /**
             * 手动确认消息
             * deliveryTag:该消息的index
             *  false只确认当前一个消息收到，true确认所有consumer获得的消息（成功消费，消息从队列中删除 ）
             */
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            try{
                /**
                 * 重回队列
                 * deliveryTag:该消息的index
                 * multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
                 * requeue：被拒绝的是否重新入队列
                 */
                //重回队列
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
