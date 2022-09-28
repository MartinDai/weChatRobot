package com.doodl6.wechatrobot.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 响应消息基类（公众帐号 -> 普通用户）
 */
@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "xml")
public class BaseMessage {

    /**
     * 接收方帐号（收到的OpenID）
     **/
    @JacksonXmlCData
    @JsonProperty("ToUserName")
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;

    /**
     * 开发者微信号
     **/
    @JacksonXmlCData
    @JsonProperty("FromUserName")
    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;

    /**
     * 消息创建时间
     **/
    @JacksonXmlProperty(localName = "CreateTime")
    private Long createTime;

    /**
     * 消息类型
     **/
    @JacksonXmlCData
    @JsonProperty("MsgType")
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;

}
