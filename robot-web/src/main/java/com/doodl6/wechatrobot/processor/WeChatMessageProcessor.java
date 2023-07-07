package com.doodl6.wechatrobot.processor;

import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.enums.WeChatMsgType;
import com.doodl6.wechatrobot.response.BaseMessage;

/**
 * 处理微信消息接口类
 */
public interface WeChatMessageProcessor {

    WeChatMsgType getMsgType();

    BaseMessage processMessage(WeChatMessage weChatMessage);
}
