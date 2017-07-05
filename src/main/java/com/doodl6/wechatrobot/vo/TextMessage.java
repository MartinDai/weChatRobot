package com.doodl6.wechatrobot.vo;

import com.doodl6.wechatrobot.util.Constants;

/**
 * 响应消息-> 文本消息
 */
public class TextMessage extends BaseMessage {

	/** 内容 **/
	private String Content;

	public TextMessage(String fromUserName, String toUserName, String content) {
		super(fromUserName, toUserName);
		super.setMsgType(Constants.RESP_TEXT_TYPE);
		this.Content = content;
	}

	public TextMessage() {
		super();
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
