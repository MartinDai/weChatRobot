package com.doodl6.wechatrobot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信事件类型
 */
@Getter
@AllArgsConstructor
public enum WeChatEventType {
    /**
     * 订阅
     */
    SUBSCRIBE("subscribe"),
    /**
     * 取消订阅
     */
    UNSUBSCRIBE("unsubscribe");

    private final String value;

    public static WeChatEventType findByValue(String value) {
        for (WeChatEventType eventType : WeChatEventType.values()) {
            if (eventType.getValue().equals(value)) {
                return eventType;
            }
        }
        return null;
    }
}
