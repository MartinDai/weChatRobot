package com.mandy.wechatrobot.vo;

import com.mandy.wechatrobot.util.Constants;

/**
 * 响应消息-> 文本消息
 */
public class TextMessage extends BaseMessage {

	/** 内容 **/
	private String content;

	public TextMessage(String fromUserName, String toUserName, String content) {
		super(fromUserName, toUserName);
		super.setMsgType(Constants.RESP_TEXT_TYPE);
		this.content = content;
	}

	public TextMessage() {
		super();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
