package com.mandy.wechatrobot.handle;

import java.util.Map;

/**
 * 处理微信消息接口类
 *
 */
public interface WeChatMessageHandle {

	public String processMessage(Map<String, String> parameters)
			throws Exception;
}
