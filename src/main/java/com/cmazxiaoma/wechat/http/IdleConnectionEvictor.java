package com.cmazxiaoma.wechat.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.HttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class IdleConnectionEvictor extends Thread {

    @Autowired
    private HttpClientConnectionManager httpClientConnectionManager;

    private volatile boolean shutdown;

    public IdleConnectionEvictor () {
        super();
        super.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    //关闭失效的连接
                    httpClientConnectionManager.closeExpiredConnections();;
                }
            }
        } catch (InterruptedException ex) {
            log.error("关闭失效的连接发生错误 = {}", ex.getMessage());
        }
    }

    public void shutdown() {
        shutdown = true;

        synchronized (this) {
            notifyAll();
        }
    }
}
