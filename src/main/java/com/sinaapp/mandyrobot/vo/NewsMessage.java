package com.sinaapp.mandyrobot.vo;

import java.util.List;

import com.sinaapp.mandyrobot.util.Constants;

/**
 * 响应消息-> 图文消息
 */
public class NewsMessage extends BaseMessage {

	/** 文章数量，限制为10条以内 **/
	private int ArticleCount;
	/** 文章列表默认第一个item为大图 **/
	private List<Article> Articles;

	public NewsMessage() {
		super();
	}

	public NewsMessage(String fromUserName, String toUserName) {
		super(fromUserName, toUserName);
		super.setMsgType(Constants.RESP_NEWS_TYPE);
	}

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}

}
