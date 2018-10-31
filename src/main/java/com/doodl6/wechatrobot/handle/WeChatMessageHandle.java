package com.doodl6.wechatrobot.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 处理微信消息接口类
 */
public interface WeChatMessageHandle {

    Logger LOGGER = LoggerFactory.getLogger(WeChatMessageHandle.class);

    String processMessage(Map<String, String> parameters)
            throws Exception;
}
