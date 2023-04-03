package com.hgq.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * rabbitMQ 消费者
 *
 * @Author hgq
 * @Date: 2022-06-28 16:38
 * @since 1.0
 **/
@Component
public class RabbitMqConsumer {

    /**
     * 1. 手动创建:
     * 需在RabbitMQ中手动创建 order 队列，否则报错
     * 例如：@RabbitListener(queues = "orderQueue",concurrency = "10") ，concurrency是消费线程数
     * 2. 自动创建：
     *
     * @param body
     * @param headers
     * @RabbitListener(queuesToDeclare = @Queue(name = "orderQueue"), concurrency = "10")
     * 3. 自动创建队列，Exchange 与 Queue绑定
     * @RabbitListener(bindings = @QueueBinding(value = @Queue(“orderQueue”), exchange = @Exchange(“orderEchange”)))
     */
//    @RabbitListener(queues = "orderQueue",concurrency = "10")
//    @RabbitListener(bindings = @QueueBinding(value = @Queue("orderQueue"), exchange = @Exchange("orderEchange")), concurrency = "10")
//    @RabbitListener(queuesToDeclare = @Queue(name = "orderQueue"), concurrency = "10")
    @RabbitListener
    public void processMessage1(@Payload String body, @Headers Map<String, Object> headers, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG)long deliveryTag) {
        System.out.println("body：" + body);
        System.out.println("Headers：" + headers);

    }


/*    @RabbitListener(queues = "orderQueue")
    public void processMessage1(@Payload String body, @Header String token) {
        System.out.println("body：" + body);
        System.out.println("token：" + token);
    }*/

/*    @RabbitListener(queues = "orderQueue")
    public void receive(Message message, Channel channel) throws IOException {
        System.out.println("receive=" + message.getPayload());
        channel.basicAck(((Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG)), true);
    }*/

/*    @RabbitListener(queues = "order", concurrency = "10")
    public void receive2(Message message, Channel channel) throws IOException {
        try {
            System.out.println("receive2 = " + message.getBody() + "------->" + Thread.currentThread().getName());
            //拒绝
            channel.basicReject(((Long) message.getMessageProperties().getHeaders().get(AmqpHeaders.DELIVERY_TAG)), true);
            //确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("消息消费异常，重回队列");
            //回退
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }*/
}
