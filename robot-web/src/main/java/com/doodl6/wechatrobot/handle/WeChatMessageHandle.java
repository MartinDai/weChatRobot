package com.doodl6.wechatrobot.handle;

import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.response.BaseMessage;

/**
 * 处理微信消息接口类
 */
public interface WeChatMessageHandle {

    BaseMessage processMessage(WeChatMessage weChatMessage);
}
