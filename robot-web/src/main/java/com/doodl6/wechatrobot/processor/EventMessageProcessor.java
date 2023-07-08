package com.doodl6.wechatrobot.processor;

import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.enums.WeChatEventType;
import com.doodl6.wechatrobot.enums.WeChatMsgType;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;

import java.util.logging.Logger;

/**
 * 事件类型消息处理类
 */
public class EventMessageProcessor implements WeChatMessageProcessor {

    private static final Logger LOGGER = Logger.getLogger(EventMessageProcessor.class.getName());

    @Override
    public WeChatMsgType getMsgType() {
        return WeChatMsgType.EVENT;
    }

    @Override
    public BaseMessage processMessage(WeChatMessage weChatMessage) {
        String event = weChatMessage.getEvent();
        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        WeChatEventType eventType = WeChatEventType.findByValue(event);
        if (eventType == WeChatEventType.SUBSCRIBE) {
            return new TextMessage(toUserName, fromUserName, "谢谢关注！可以开始跟我聊天啦😁");
        } else if (eventType == WeChatEventType.UNSUBSCRIBE) {
            LOGGER.info("用户[" + weChatMessage.getFromUserName() + "]取消了订阅");
        }

        return new TextMessage(toUserName, fromUserName, "bye!");
    }
}
