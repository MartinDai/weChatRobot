package com.sinaapp.mandyrobot.handle;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.sinaapp.mandyrobot.util.Constants;
import com.sinaapp.mandyrobot.util.MessageUtil;
import com.sinaapp.mandyrobot.vo.TextMessage;

/**
 * 处理事件类型消息
 */
@Service("eventMessageHandle")
public class EventMessageHandle implements WeChatMessageHandle {

	@Override
	public String processMessage(final Map<String, String> parameters) {
		String eventType = parameters.get("Event");
		String fromUserName = parameters.get("FromUserName");
		String toUserName = parameters.get("toUserName");
		if (Constants.REQ_SUBSCRIBE_TYPE.equals(eventType)) {
			return MessageUtil.ObjectToXml(new TextMessage(toUserName,
					fromUserName, "感谢您的关注！"));
		} else if (Constants.REQ_UNSUBSCRIBE_TYPE.equals(eventType)) {

		} else if (Constants.REQ_EVENT_TYPE.equals(eventType)) {

		}
		return "";
	}
}
