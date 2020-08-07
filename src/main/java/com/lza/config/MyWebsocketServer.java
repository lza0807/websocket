package com.lza.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gjing
 **/
@ServerEndpoint("/ws/{userId}")
@Component
@Slf4j
public class MyWebsocketServer {
    /**
     * 存放所有在线的客户端
     */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
     */
    private static int onlineCount = 0;


    /**
     * 接收 sid
     */
    private String userId = "";



    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws IOException {
        this.userId = userId;

        //将新用户存入在线的组
        clients.put(userId, session);

        addOnlineCount(); // 在线数 +1

        log.info("有新窗口开始监听:" + userId + ",当前在线人数为" + getOnlineCount());

        sendAll(JSON.toJSONString("连接成功:"),userId);
    }

    /**
     * 客户端关闭
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        subOnlineCount(); // 人数 -1
        //将掉线的用户移除在线的组里
        if (clients.get(this.userId)!=null){
            clients.remove(this.userId);
        }
        log.info("有用户断开了, id为:{}", session.getId());
    }

    /**
     * 发生错误
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 收到客户端发来消息
     * @param message  消息对象
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("服务端收到客户端发来的消息: {}", message);
        sendAll(message,userId);
    }

    /**
     * 实现服务器主动推送
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        clients.get(this.userId).getBasicRemote().sendText(message);
    }

    /**
     * 群发消息
     * @param message 消息内容
     */
    public static void sendAll(String message, @PathParam("userId") String userId) throws IOException {
        // 遍历集合，可设置为推送给指定sid，为 null 时发送给所有人
        Iterator entrys = clients.entrySet().iterator();
        while (entrys.hasNext()) {
            Map.Entry entry = (Map.Entry) entrys.next();

            if (userId == null) {
                clients.get(entry.getKey()).getBasicRemote().sendText(message);
                log.info("发送消息到：" + entry.getKey() + "，消息：" + message);
            } else if (entry.getKey().equals(userId)) {
                clients.get(entry.getKey()).getBasicRemote().sendText(message);
                log.info("发送消息到：" + entry.getKey() + "，消息：" + message);
            }

        }
    }


    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        MyWebsocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        MyWebsocketServer.onlineCount--;
    }
}
