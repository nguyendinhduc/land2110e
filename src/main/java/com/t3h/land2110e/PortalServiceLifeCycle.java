package com.t3h.land2110e;

import com.t3h.land2110e.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class PortalServiceLifeCycle implements CommandLineRunner {
    @Autowired
    private SocketService socketService;

    @Override
    public void run(String... arg0) throws Exception {
    }

    @PreDestroy
    public void onExit() {
        socketService.getSocketIOServer().stop();
    }
}
