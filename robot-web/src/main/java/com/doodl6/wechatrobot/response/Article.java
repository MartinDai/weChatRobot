package com.doodl6.wechatrobot.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("Title")
    @JacksonXmlProperty(localName = "Title")
    private String title;

    /**
     * 文章描述
     **/
    @JacksonXmlCData
    @JsonProperty("Description")
    @JacksonXmlProperty(localName = "Description")
    private String description;

    /**
     * 封面地址
     **/
    @JacksonXmlCData
    @JsonProperty("PicUrl")
    @JacksonXmlProperty(localName = "PicUrl")
    private String picUrl;

    /**
     * 文章地址
     **/
    @JacksonXmlCData
    @JsonProperty("Url")
    @JacksonXmlProperty(localName = "Url")
    private String url;

}
