package com.doodl6.wechatrobot.handle;

import com.doodl6.wechatrobot.enums.WeChatEventType;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 事件类型消息处理类
 */
@Service
public class EventMessageHandle implements WeChatMessageHandle {

    @Override
    public String processMessage(final Map<String, String> parameters) {
        String event = parameters.get("Event");
        String fromUserName = parameters.get("FromUserName");
        String toUserName = parameters.get("toUserName");
        WeChatEventType eventType = WeChatEventType.findByValue(event);
        if (eventType == WeChatEventType.SUBSCRIBE) {
            return MessageUtil.ObjectToXml(new TextMessage(toUserName,
                    fromUserName, "感谢您的关注！"));
        }

        return StringUtils.EMPTY;
    }
}
