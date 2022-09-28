package com.doodl6.wechatrobot.util;

/**
 * 日志工具类
 */
public final class LogUtil {

    private LogUtil() {
    }

    /**
     * 构建日志字符串
     */
    public static String buildLog(Object... objArray) {
        StringBuilder logBuilder = new StringBuilder();

        for (Object obj : objArray) {
            if (logBuilder.length() > 0) {
                logBuilder.append(" | ");
            }

            if (obj instanceof String) {
                logBuilder.append(obj);
            } else {
                logBuilder.append(JsonUtil.objToJson(obj));
            }
        }

        return logBuilder.toString();
    }
}
