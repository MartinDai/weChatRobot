package com.doodl6.wechatrobot.vo;

/**
 * 文章实体类
 */
public class Article {
	/** 文章标题 **/
	private String Title;
	/** 文章描述 **/
	private String Description;
	/** 封面地址 **/
	private String PicUrl;
	/** 文章地址 **/
	private String Url;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

}
