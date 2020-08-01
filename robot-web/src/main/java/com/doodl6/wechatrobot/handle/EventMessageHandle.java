package com.doodl6.wechatrobot.handle;

import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.enums.WeChatEventType;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 事件类型消息处理类
 */
@Service
public class EventMessageHandle implements WeChatMessageHandle {

    private static final Logger logger = LoggerFactory.getLogger(EventMessageHandle.class);

    @Override
    public String processMessage(WeChatMessage weChatMessage) {
        String event = weChatMessage.getEvent();
        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        WeChatEventType eventType = WeChatEventType.findByValue(event);
        if (eventType == WeChatEventType.SUBSCRIBE) {
            return MessageUtil.toXml(new TextMessage(toUserName,
                    fromUserName, "谢谢关注！可以开始跟我聊天啦😁"));
        } else if (eventType == WeChatEventType.UNSUBSCRIBE) {
            logger.info("用户[" + weChatMessage.getFromUserName() + "]取消了订阅");
        }

        return StringUtils.EMPTY;
    }
}
