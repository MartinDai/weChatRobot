package com.doodl6.wechatrobot.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 文章响应类
 */
@Getter
@Setter
public class Article {

    /**
     * 文章标题
     **/
    private String Title;

    /**
     * 文章描述
     **/
    private String Description;

    /**
     * 封面地址
     **/
    private String PicUrl;

    /**
     * 文章地址
     **/
    private String Url;

}
