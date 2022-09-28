package com.doodl6.wechatrobot.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
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
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Title")
    private String title;

    /**
     * 文章描述
     **/
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Description")
    private String description;

    /**
     * 封面地址
     **/
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "PicUrl")
    private String picUrl;

    /**
     * 文章地址
     **/
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Url")
    private String url;

}
