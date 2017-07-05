package com.doodl6.wechatrobot.vo;

import java.util.Date;

/**
 * 响应消息基类（公众帐号 -> 普通用户）
 * 
 */
public class BaseMessage {

	/** 接收方帐号（收到的OpenID） **/
	private String ToUserName;
	/** 开发者微信号 **/
	private String FromUserName;
	/** 消息创建时间 **/
	private long CreateTime;
	/** 消息类型 **/
	private String MsgType;

	public BaseMessage() {
		super();
	}

	public BaseMessage(String fromUserName, String toUserName) {
		super();
		FromUserName = fromUserName;
		ToUserName = toUserName;
		CreateTime = new Date().getTime();
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

}
