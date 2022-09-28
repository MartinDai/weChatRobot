package com.doodl6.wechatrobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.doodl6.wechatrobot")
public class WebStarter {

    public static void main(String[] args) {
        SpringApplication.run(WebStarter.class, args);
    }

}
