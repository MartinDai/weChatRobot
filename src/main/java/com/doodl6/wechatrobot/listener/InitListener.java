package com.doodl6.wechatrobot.listener;

import com.doodl6.wechatrobot.constant.AppConstants;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * 初始化监听
 */
public class InitListener implements ServletContextListener {

    private static Logger LOGGER = Logger.getLogger(InitListener.class);

    public void contextInitialized(ServletContextEvent sce) {

        Properties properties = new Properties();
        try {
            properties.load(InitListener.class.getClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException e) {
            LOGGER.error("加载config.properties出错！");
        }

        AppConstants.APP_ID = properties.getProperty("appId");
        AppConstants.APP_SECRET = properties.getProperty("appSecret");
        AppConstants.TOKEN = properties.getProperty("token");
        AppConstants.API_KEY = properties.getProperty("apiKey");

    }

    public void contextDestroyed(ServletContextEvent sce) {
    }

}
