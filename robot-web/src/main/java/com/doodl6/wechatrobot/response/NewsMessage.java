package com.doodl6.wechatrobot.response;

import com.doodl6.wechatrobot.enums.MessageType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 响应消息-> 图文消息
 */
@Getter
@Setter
public class NewsMessage extends BaseMessage {

    /**
     * 文章数量，限制为10条以内
     **/
    private int ArticleCount;

    /**
     * 文章列表默认第一个item为大图
     **/
    private List<Article> Articles;

    public NewsMessage() {
        super();
        setMsgType(MessageType.NEWS.getValue());
    }

    public NewsMessage(List<Article> articles) {
        this();
        Assert.notEmpty(articles, "文章列表不能为空");
        this.Articles = articles;
        this.ArticleCount = articles.size();
    }

}
