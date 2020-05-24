package com.doodl6.wechatrobot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    /**
     * 文本消息
     */
    TEXT("text"),
    /**
     * 图文消息
     */
    NEWS("news");

    private final String value;

}
