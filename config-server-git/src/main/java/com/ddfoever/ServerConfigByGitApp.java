package com.ddfoever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ServerConfigByGitApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerConfigByGitApp.class,args);
    }
}
