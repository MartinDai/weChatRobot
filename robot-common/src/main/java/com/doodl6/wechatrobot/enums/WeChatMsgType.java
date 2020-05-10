package com.doodl6.wechatrobot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信消息类型
 */
@Getter
@AllArgsConstructor
public enum WeChatMsgType {
    /**
     * 事件
     */
    EVENT("event"),
    /**
     * 文本
     */
    TEXT("text");

    private final String value;

    public static WeChatMsgType findByValue(String value) {
        for (WeChatMsgType msgType : WeChatMsgType.values()) {
            if (msgType.getValue().equals(value)) {
                return msgType;
            }
        }
        return null;
    }
}
