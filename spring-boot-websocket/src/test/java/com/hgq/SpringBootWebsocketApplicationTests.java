package com.hgq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SpringBootWebsocketApplicationTests {

    @Test
    void contextLoads() {
    }

    //连接数
    public static int connectNum = 0;
    //连接成功数
    public static int successNum = 0;
    //连接失败数
    public static int errorNum = 0;

    /**
     * 测试websocket最大连接数
     *
     * @throws InterruptedException
     */
    @Test
    public void testConnect() throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //每次3秒打印一次连接结果
                    System.out.println(System.currentTimeMillis() +
                            "  连接数：" + connectNum
                            + "  成功数：" + successNum
                            + "  失败数：" + errorNum);
                }
            }
        }.start();
        List<WebSocketStompClient> list = new ArrayList<>();
        System.out.println("开始时间：" + System.currentTimeMillis());
        while (true) {
            //连接失败超过10次，停止测试
            if (errorNum > 10) {
                break;
            }
            list.add(newConnect(++connectNum));
            Thread.sleep(10);
        }
    }

    /**
     * 创建websocket连接
     *
     * @param i
     * @return
     */
    private WebSocketStompClient newConnect(int i) {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient socketClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(socketClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        stompClient.setTaskScheduler(taskScheduler);

        String url = "ws://localhost:8888/bullet";
        stompClient.connect(url, new TestConnectHandler());
        return stompClient;
    }

    private static synchronized void addSuccessNum() {
        successNum++;
    }

    private static synchronized void addErrorNum() {
        errorNum++;
    }

    private static class TestConnectHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            addSuccessNum();
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            addErrorNum();
        }
    }

}
