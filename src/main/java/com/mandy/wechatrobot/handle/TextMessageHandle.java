package com.mandy.wechatrobot.handle;

import java.net.URLEncoder;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mandy.wechatrobot.util.Constants;
import com.mandy.wechatrobot.util.HttpUtil;
import com.mandy.wechatrobot.util.MessageUtil;
import com.mandy.wechatrobot.util.PropertiesLoader;

/**
 * 处理文本类型消息
 */
@Service("textMessageHandle")
public class TextMessageHandle implements WeChatMessageHandle {

	@Override
	public String processMessage(final Map<String, String> parameters)
			throws Exception {
		String fromUserName = parameters.get("FromUserName");
		String toUserName = parameters.get("ToUserName");
		String content = parameters.get("Content");
		String appKey = PropertiesLoader.API_KEY;
		String info = URLEncoder.encode(content, "utf-8");
		String requestUrl = Constants.TURING_API_URL + "?key=" + appKey
				+ "&info=" + info + "&userid=" + fromUserName;
		String result = HttpUtil.get(requestUrl);
		Object obj = MessageUtil.processTuRingResult(result, toUserName,
				fromUserName);
		return MessageUtil.ObjectToXml(obj);
	}
}
