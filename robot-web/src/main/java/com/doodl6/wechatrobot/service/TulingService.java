package com.doodl6.wechatrobot.service;

import com.doodl6.wechatrobot.config.AppConfig;
import com.doodl6.wechatrobot.constant.TulingConstants;
import com.doodl6.wechatrobot.response.Article;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.NewsMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.HttpUtil;
import com.doodl6.wechatrobot.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

/**
 * 图灵服务类
 */
@Service
public class TulingService {

    private static final String API_URL = "http://www.tuling123.com/openapi/api";

    @Resource
    private AppConfig appConfig;

    /**
     * 获取图灵机器人消息响应
     */
    public BaseMessage getTulingResponse(String content, String fromUserName) {
        String info = new String(content.getBytes(), StandardCharsets.UTF_8);
        String requestUrl = API_URL + "?key=" + appConfig.getApiKey() + "&info=" + info + "&userid=" + fromUserName;
        String result = HttpUtil.get(requestUrl);
        return processTuRingResult(result);
    }

    /**
     * 处理图灵机器人返回的结果
     */
    private static BaseMessage processTuRingResult(String result) {
        JsonNode rootObj = JsonUtil.jsonToObj(result, JsonNode.class);
        int code = rootObj.get("code").asInt();
        if (TulingConstants.TEXT_CODE.equals(code)) {
            return new TextMessage(rootObj.get("text").asText());
        } else if (TulingConstants.LINK_CODE.equals(code)) {
            return new TextMessage("<a href='" + rootObj.get("url").asText() + "'>"
                    + rootObj.get("text").asText() + "</a>");
        } else if (TulingConstants.NEWS_CODE.equals(code) || TulingConstants.TRAIN_CODE.equals(code)
                || TulingConstants.FLIGHT_CODE.equals(code) || TulingConstants.MENU_CODE.equals(code)) {
            List<Article> articles = convertToArticles((ArrayNode) rootObj.get("list"), code);
            return new NewsMessage(articles);
        } else if (TulingConstants.LENGTH_WRONG_CODE.equals(code) || TulingConstants.KEY_WRONG_CODE.equals(code)) {
            return new TextMessage("我现在想一个人静一静,请等下再跟我聊天");
        } else if (TulingConstants.EMPTY_CONTENT_CODE.equals(code)) {
            return new TextMessage("你不说话,我也没什么好说的");
        } else if (TulingConstants.NUMBER_DONE_CODE.equals(code)) {
            return new TextMessage("我今天有点累了,明天再找我聊吧！");
        } else if (TulingConstants.NOT_SUPPORT_CODE.equals(code)) {
            return new TextMessage("这个我还没学会,我以后会慢慢学的");
        } else if (TulingConstants.UPGRADE_CODE.equals(code)) {
            return new TextMessage("我经验值满了,正在升级中...");
        } else if (TulingConstants.DATA_EXCEPTION_CODE.equals(code)) {
            return new TextMessage("你都干了些什么,我怎么话都说不清楚了");
        }

        return null;
    }

    /**
     * 根据消息类型转换文章列表
     */
    private static List<Article> convertToArticles(ArrayNode arrayNode, int code) {
        String titleKey = "article";
        String descriptionKey = "article";
        if (TulingConstants.MENU_CODE.equals(code)) {
            titleKey = "name";
            descriptionKey = "info";
        }
        List<Article> articles = Lists.newLinkedList();
        Iterator<JsonNode> nodeIterator = arrayNode.elements();
        while (nodeIterator.hasNext()) {
            JsonNode jsonNode = nodeIterator.next();
            Article article = new Article();
            if (TulingConstants.TRAIN_CODE.equals(code)) {
                article.setTitle(jsonNode.get("trainnum").asText() + " —— 开车时间:" + jsonNode.get("starttime").asText());
                article.setDescription(jsonNode.get("start").asText() + "(" + jsonNode.get("starttime").asText() + ")——>"
                        + jsonNode.get("terminal").asText() + "(" + jsonNode.get("endtime").asText() + ")");
            } else if (TulingConstants.FLIGHT_CODE.equals(code)) {
                article.setTitle(jsonNode.get("flight").asText() + " —— 起飞时间:" + jsonNode.get("starttime").asText());
                article.setDescription(jsonNode.get("route").asText() + jsonNode.get("starttime").asText() + "——>"
                        + jsonNode.get("endtime").asText() + "\n航班状态:" + jsonNode.get("state").asText());
            } else {
                article.setTitle(jsonNode.get(titleKey).asText());
                article.setDescription(jsonNode.get(descriptionKey).asText());
            }
            article.setPicUrl(jsonNode.get("icon").asText());
            article.setUrl(jsonNode.get("detailurl").asText());
            articles.add(article);
            if (articles.size() == 10) {
                break;
            }
        }

        return articles;
    }
}
