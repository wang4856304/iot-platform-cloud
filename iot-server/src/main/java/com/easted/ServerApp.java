package com.easted;

import com.easted.netty.Server;
import com.easted.service.MessageInitialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @Author wangJun
 * @Description //
 * @Date 2019/10/11 10:22:30
 **/

@SpringBootApplication
public class ServerApp implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(ServerApp.class);

    @Resource
    private Server server;

    public static void main(String args[]) {
        SpringApplication.run(ServerApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("服务启动.......");
        MessageInitialize.init();
        new Thread(() -> server.start()).start();

    }
}
