package com.mandy.wechatrobot.listener;

import com.mandy.wechatrobot.constant.AppConstants;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * 初始化监听
 */
public class InitListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger(InitListener.class);

    public void contextDestroyed(ServletContextEvent sce) {
    }

    public void contextInitialized(ServletContextEvent sce) {

        Properties properties = new Properties();
        try {
            properties.load(InitListener.class.getClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException e) {
            logger.error("加载config.properties出错！");
        }

        AppConstants.APP_ID = properties.getProperty("appId");
        AppConstants.APP_SECRET = properties.getProperty("appSecret");
        AppConstants.TOKEN = properties.getProperty("token");
        AppConstants.API_KEY = properties.getProperty("appKey");

    }

}
