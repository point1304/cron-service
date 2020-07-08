package com.ksyim.hellocron.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HelloCronApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloCronApplication.class, args);
    }
}
