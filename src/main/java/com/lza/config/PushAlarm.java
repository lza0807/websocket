package com.lza.config;

import com.alibaba.fastjson.JSON;
import com.lza.websocket.mapper.MessageMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * lza
 */
@Slf4j
@Component
@Order(1)   // 如果多个自定义ApplicationRunner，用来表明执行顺序
public class PushAlarm implements ApplicationRunner {   // 服务启动后自动加载该类

    @Autowired
    private MessageMapper messageMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("------------->" + "项目启动，now =" + new Date());
        this.myTimer();
    }

    public void myTimer() {

        String userId = null; // userId 为空时，会推送给连接此 WebSocket 的所有人

        Runnable runnable1 = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    String message = JSON.toJSONString(messageMapper.selectList(null)); // 第三方接口返回数据
                    MyWebsocketServer.sendAll(message, userId);
                    Thread.sleep(5000);
                }
            }
        };


        Thread thread1 = new Thread(runnable1);

        thread1.start();

    }

}

