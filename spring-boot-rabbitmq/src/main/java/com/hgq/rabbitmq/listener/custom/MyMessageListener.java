package com.hgq.rabbitmq.listener.custom;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-07 11:21
 * @since 1.0
 **/
@Component
public class MyMessageListener implements ChannelAwareMessageListener {

    /**
     * 消费消息确认
     * 消息确认主要分为两种模式：
     *
     * 自动确认
     *
     * 这是rabbitmq的默认模式，也就是我们之前直接添加RabbitHanlder所选用的模式。这种模式下，只要消费端成功拿到消息即算确认成功，无论消费端后续处理消息是否成功。所以有个很明显的问题，就是如果消息处理失败则意味着该消息被直接抛弃了，不会有其他消费端进行处理。
     *
     * 手动确认
     *
     * 这就是我们常用的模式，在我们消费端收到消息之后，根据处理情况手动调用确认方式：
     *
     * basicAck ：肯定确认
     * basicNack：否定确认，可以批量确认
     * basicReject：否定确认，只能一次拒绝一条消息
     * void basicNack(deliveryTag,multiple,requeue)：
     *
     * deliveryTag：消息唯一标识，自动生成，通过message.getMessageProperties().getDeliveryTag获得
     *
     * multiple：是否开启批量退回，false则不开启，开启退回需要增加自己的业务判断逻辑（例如攒够几条再批量退回等等）
     *
     * requeue是否退回到消息队列，true表示退回，交给其他消费者处理，不退回则直接丢弃
     *
     * 原文链接：https://blog.csdn.net/Cwh_971111/article/details/118208805
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
//       类似于消息id
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try{
//            对消息的自定义处理
            System.out.println("Message:"+message.toString());
            System.out.println("消息来自："+message.getMessageProperties().getConsumerQueue());
//          确认消息
//          第二个参数会是否开启批处理，true则表示一次性确认小于等于传入值的所有消息
            channel.basicAck(deliveryTag,true);

        }catch (Exception e){
            channel.basicNack(deliveryTag,true,false);
            e.printStackTrace();
        }
    }
}
