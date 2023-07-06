package com.doodl6.wechatrobot.util;

public class PropertyUtil {

    public static String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key);
        }

        return value;
    }
}
