package com.doodl6.wechatrobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = {
        "com.doodl6.wechatrobot"
})
@ServletComponentScan
public class WebStarter {

    public static void main(String[] args) {
        SpringApplication.run(WebStarter.class, args);
    }

}
