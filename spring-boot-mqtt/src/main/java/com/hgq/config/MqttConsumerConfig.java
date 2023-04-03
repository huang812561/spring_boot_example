package com.hgq.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.concurrent.ExecutionException;

/**
 * mqtt配置类
 *
 * @Author hgq
 * @Date: 2022-03-29 16:53
 * @since 1.0
 **/
@Configuration
@IntegrationComponentScan
@Slf4j
public class MqttConsumerConfig {

    @Autowired
    private MqttConsumerProperties properties;

    @Bean
    public MqttConnectOptions getMqttConsumerConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(10);
        mqttConnectOptions.setKeepAliveInterval(60);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(properties.getUserName());
        mqttConnectOptions.setPassword(properties.getPassword().toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{properties.getUrl()});
        return mqttConnectOptions;
    }


    @Bean
    public MqttPahoClientFactory mqttConsumerClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConsumerConnectOptions());
        return factory;
    }

    /******************MQTT 消费者配置*********************/

    //--------------------------------- 接收消息部分 ------------------------------------------

    /**
     * MQTT接收消息 - 订阅消息
     *
     * @return
     */
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                properties.getConsumerClientId(),
                mqttConsumerClientFactory(),
                properties.getConsumerTopics());
        adapter.setCompletionTimeout(10000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        /**
         * 设置服务质量
         *  0 最多一次，数据可能丢失;
         *  1 至少一次，数据可能重复;
         *  2 只有一次，有且只有一次;最耗性能
         */
        adapter.setQos(1);
        /**
         * 设置订阅通道
         */
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;

    }

    /**
     * MQTT信息通道（消费者）
     *
     * @return {@link MessageChannel}
     */
    @Bean(name = "mqttInboundChannel")
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }


    /**
     * MQTT消息处理器（消费者）
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String payload = message.getPayload().toString();
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                //用线程池处理订阅到的消息
                try {
                    dealMessageByThreadPool(payload, topic);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    log.error("处理订阅出错：" + e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * @param
     * @return 多线程处理收到的传感器推送, 线程处理参考：
     * https://www.bbsmax.com/A/mo5kxvPvdw/（这里注释掉线程的使用，保持数据的一致性
     * 在处理某一次上报的数据时可以用多线程，提高效率，不能每次订阅数据都起一个线程让它单独处理）
     * 要根据厂商提交的设备id及主题先把所有设备的主题管理起来，一个设备对应一个或多个主题
     * @author huangquanguang
     * @date 2020/2/21 11:54
     */
    public void dealMessageByThreadPool(String payload, String topic) throws ExecutionException, InterruptedException {
        //保存mongodb部分
        log.info("接收到的topic:{}, 处理消息内容：{} ", topic, payload);
    }


}
