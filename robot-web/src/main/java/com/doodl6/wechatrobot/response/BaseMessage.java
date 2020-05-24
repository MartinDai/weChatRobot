package com.doodl6.wechatrobot.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 响应消息基类（公众帐号 -> 普通用户）
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseMessage {

    /**
     * 接收方帐号（收到的OpenID）
     **/
    private String ToUserName;

    /**
     * 开发者微信号
     **/
    private String FromUserName;

    /**
     * 消息创建时间
     **/
    private Long CreateTime;

    /**
     * 消息类型
     **/
    private String MsgType;

}
