package com.mandy.wechatrobot.vo;

import java.util.List;

import com.mandy.wechatrobot.util.Constants;

/**
 * 响应消息-> 图文消息
 */
public class NewsMessage extends BaseMessage {

	/** 文章数量，限制为10条以内 **/
	private int articleCount;
	/** 文章列表默认第一个item为大图 **/
	private List<Article> articles;

	public NewsMessage() {
		super();
	}

	public NewsMessage(String fromUserName, String toUserName) {
		super(fromUserName, toUserName);
		super.setMsgType(Constants.RESP_NEWS_TYPE);
	}

	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

}
