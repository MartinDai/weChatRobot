package com.doodl6.wechatrobot.response;


import com.doodl6.wechatrobot.enums.MessageType;
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
    private String Content;

    public TextMessage() {
        super();
        setMsgType(MessageType.TEXT.getValue());
    }

    public TextMessage(String content) {
        this();
        this.Content = content;
    }

    public TextMessage(String fromUserName, String toUserName, String content) {
        this(content);
        setFromUserName(fromUserName);
        setToUserName(toUserName);
        setCreateTime(System.currentTimeMillis());
    }
}
