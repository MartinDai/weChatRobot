package com.doodl6.wechatrobot.handle;

import com.doodl6.wechatrobot.domain.WeChatMessage;

/**
 * 处理微信消息接口类
 */
public interface WeChatMessageHandle {

    String processMessage(WeChatMessage weChatMessage);
}
