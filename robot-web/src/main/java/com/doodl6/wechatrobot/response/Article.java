package com.doodl6.wechatrobot.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章响应类
 */
@Getter
@Setter
@XStreamAlias("item")
public class Article {

    /**
     * 文章标题
     **/
    @XStreamAlias("Title")
    private String title;

    /**
     * 文章描述
     **/
    @XStreamAlias("Description")
    private String description;

    /**
     * 封面地址
     **/
    @XStreamAlias("PicUrl")
    private String picUrl;

    /**
     * 文章地址
     **/
    @XStreamAlias("Url")
    private String url;

}
