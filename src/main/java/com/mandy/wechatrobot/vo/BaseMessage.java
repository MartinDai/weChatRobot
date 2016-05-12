package com.mandy.wechatrobot.vo;

import java.util.Date;

/**
 * 响应消息基类（公众帐号 -> 普通用户）
 * 
 */
public class BaseMessage {

	/** 接收方帐号（收到的OpenID） **/
	private String toUserName;
	/** 开发者微信号 **/
	private String fromUserName;
	/** 消息创建时间 **/
	private long createTime;
	/** 消息类型 **/
	private String msgType;

	public BaseMessage() {
		super();
	}

	public BaseMessage(String fromUserName, String toUserName) {
		super();
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
		this.createTime = new Date().getTime();
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

}
