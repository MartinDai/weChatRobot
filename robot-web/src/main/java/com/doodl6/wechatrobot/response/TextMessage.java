package com.doodl6.wechatrobot.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 响应消息-> 文本消息
 */
@Getter
@Setter
@NoArgsConstructor
public class TextMessage extends BaseMessage {

    /**
     * 内容
     **/
    private String Content;

    public TextMessage(String fromUserName, String toUserName, String content) {
        super(fromUserName, toUserName);
        setMsgType("text");
        this.Content = content;
    }

}
