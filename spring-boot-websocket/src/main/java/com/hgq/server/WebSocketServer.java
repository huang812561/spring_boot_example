package com.hgq.server;

import com.alibaba.fastjson.JSON;
import com.hgq.entity.MessageVO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket服务端,多例的，一次websocket连接对应一个实例
 *
 * @ServerEndpoint 注解的值为URI, 映射客户端输入的URL来连接到WebSocket服务器端
 * @Author hgq
 * @Date: 2022-08-24 18:57
 * @since 1.0
 **/
@Component
@ServerEndpoint("/webSocket/{sid}")
@Slf4j
public class WebSocketServer {

    /**
     * 用来记录当前在线连接数。设计成线程安全的。
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    /**
     * 用于保存uri对应的连接服务，{uri:WebSocketServer}，设计成线程安全的
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketServerMAP = new ConcurrentHashMap<>();
    @Getter
    private Session session;// 与某个客户端的连接会话，需要通过它来给客户端发送数据
    @Getter
    private String sid; //客户端消息发送者
    @Getter
    private String uri; //连接的uri

    //发送消息
    public void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            synchronized (session) {
//                System.out.println("发送数据：" + message);
                session.getBasicRemote().sendText(message);
            }
        }
    }

    /**
     * 连接建立成功时触发，绑定参数
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @param sid
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) throws IOException {
        this.session = session;
        this.sid = sid;
        this.uri = session.getRequestURI().toString();
        WebSocketServer webSocketServer = webSocketServerMAP.get(uri);
        if (webSocketServer != null) { //同样业务的连接已经在线，则把原来的挤下线。
            webSocketServer.session.getBasicRemote().sendText(uri + "重复连接被挤下线了");
            webSocketServer.session.close();//关闭连接，触发关闭连接方法onClose()
        }
        webSocketServerMAP.put(uri, this);//保存uri对应的连接服务
        addOnlineCount(); // 在线数加1

        try {
            sendMessage(session, "欢迎" + sid + "加入连接！");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 连接关闭时触发，注意不能向客户端发送消息了
     *
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException {
        webSocketServerMAP.remove(uri);//删除uri对应的连接服务
        reduceOnlineCount(); // 在线数减1
    }

    /**
     * 收到客户端消息后触发
     *
     * @param message 客户端发送过来的消息
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("收到消息：" + message);
        MessageVO messageVO = JSON.parseObject(message, MessageVO.class);
        if (messageVO==null){
            log.error("参数异常");
        }
        for (WebSocketServer webSocketServer : webSocketServerMAP.values()) {
            try {
                if(webSocketServer.getSid().equals(messageVO.getToUserId())){
                    Session session = webSocketServer.session;
                    sendMessage(session, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 通信发生错误时触发
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        try {
            log.info("{}:通信发生错误，连接关闭", this.sid);
            webSocketServerMAP.remove(uri);//删除uri对应的连接服务
        } catch (Exception e) {
        }
    }

    /**
     * 获取在线连接数
     *
     * @return
     */
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 原子性操作，在线连接数加一
     */
    public static void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    /**
     * 原子性操作，在线连接数减一
     */
    public static void reduceOnlineCount() {
        onlineCount.getAndDecrement();
    }

}
