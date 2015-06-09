package com.sinaapp.mandyrobot.handle;

import java.net.URLEncoder;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sinaapp.mandyrobot.util.Constants;
import com.sinaapp.mandyrobot.util.HttpUtil;
import com.sinaapp.mandyrobot.util.MessageUtil;
import com.sinaapp.mandyrobot.util.PropertiesLoader;

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
