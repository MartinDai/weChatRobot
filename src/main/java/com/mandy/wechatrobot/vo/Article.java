package com.mandy.wechatrobot.vo;

/**
 * 文章实体类
 */
public class Article {
	/** 文章标题 **/
	private String title;
	/** 文章描述 **/
	private String description;
	/** 封面地址 **/
	private String picUrl;
	/** 文章地址 **/
	private String url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
