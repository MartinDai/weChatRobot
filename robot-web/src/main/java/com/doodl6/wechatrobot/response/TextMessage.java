package com.doodl6.wechatrobot.response;


import com.doodl6.wechatrobot.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 响应消息-> 文本消息
 */
@Getter
@Setter
public class TextMessage extends BaseMessage {

    /**
     * 内容
     **/
    @JacksonXmlCData
    @JsonProperty("Content")
    @JacksonXmlProperty(localName = "Content")
    private String content;

    public TextMessage() {
        super();
        setMsgType(MessageType.TEXT.getValue());
    }

    public TextMessage(String content) {
        this();
        this.content = content;
    }

    public TextMessage(String fromUserName, String toUserName, String content) {
        this(content);
        setFromUserName(fromUserName);
        setToUserName(toUserName);
        setCreateTime(System.currentTimeMillis());
    }
}
