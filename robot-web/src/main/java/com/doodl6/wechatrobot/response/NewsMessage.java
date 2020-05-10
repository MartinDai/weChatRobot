package com.doodl6.wechatrobot.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 响应消息-> 图文消息
 */
@Getter
@Setter
@NoArgsConstructor
public class NewsMessage extends BaseMessage {

    /**
     * 文章数量，限制为10条以内
     **/
    private int ArticleCount;

    /**
     * 文章列表默认第一个item为大图
     **/
    private List<Article> Articles;

    public NewsMessage(String fromUserName, String toUserName) {
        super(fromUserName, toUserName);
        setMsgType("news");
    }

}
