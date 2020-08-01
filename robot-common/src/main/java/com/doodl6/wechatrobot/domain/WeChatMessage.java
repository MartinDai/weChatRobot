package com.doodl6.wechatrobot.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信消息结构类
 */
@Getter
@Setter
@XStreamAlias("xml")
public class WeChatMessage {

    @XStreamAlias("ToUserName")
    private String toUserName;

    @XStreamAlias("FromUserName")
    private String fromUserName;

    @XStreamAlias("CreateTime")
    private Long createTime;

    @XStreamAlias("MsgType")
    private String msgType;

    @XStreamAlias("Event")
    private String event;

    @XStreamAlias("EventKey")
    private String eventKey;

    @XStreamAlias("Content")
    private String content;

    @XStreamAlias("MsgId")
    private Long msgId;

}
