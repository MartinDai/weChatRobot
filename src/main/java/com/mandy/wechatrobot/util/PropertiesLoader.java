package com.mandy.wechatrobot.util;

import java.io.IOException;
import java.util.Properties;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * 配置加载类
 */
public class PropertiesLoader {

	private static Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);
	private static Properties properties = null;

	private static Properties getInterfaceProperties() {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(PropertiesLoader.class.getClassLoader().getResourceAsStream("config.properties"));
			} catch (IOException e) {
				logger.error("加载config.properties出错！");
			}
		}
		return properties;
	}

	public static String getValue(String key) {
		return getInterfaceProperties().getProperty(key);
	}

	/** 微信公众号appId **/
	public static final String APP_ID = getInterfaceProperties().getProperty("appId");
	/** 微信公众号appSecret **/
	public static final String APP_SECRET = getInterfaceProperties().getProperty("appSecret");
	/** 验证代理服务器是否可用的token,在微信开发者中心配置,为开发者随意填写 **/
	public static final String TOKEN = getInterfaceProperties().getProperty("token");
	/** 获取微信access_token的地址 **/
	public static final String WEB_ACCESS_TOKEN_URL = getInterfaceProperties().getProperty("web_access_token_url");

	/** 图灵帐号的apiKey **/
	public static final String API_KEY = getInterfaceProperties().getProperty("apiKey");
}