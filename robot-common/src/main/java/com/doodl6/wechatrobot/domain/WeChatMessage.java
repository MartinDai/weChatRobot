package com.doodl6.wechatrobot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信消息结构类
 */
@Getter
@Setter
@JsonRootName("xml")
public class WeChatMessage {

    @JsonProperty("ToUserName")
    private String toUserName;

    @JsonProperty("FromUserName")
    private String fromUserName;

    @JsonProperty("CreateTime")
    private Long createTime;

    @JsonProperty("MsgType")
    private String msgType;

    @JsonProperty("Event")
    private String event;

    @JsonProperty("EventKey")
    private String eventKey;

    @JsonProperty("Content")
    private String content;

    @JsonProperty("MsgId")
    private Long msgId;

}
