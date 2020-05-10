package com.doodl6.wechatrobot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doodl6.wechatrobot.config.AppConfig;
import com.doodl6.wechatrobot.constant.TulingConstants;
import com.doodl6.wechatrobot.response.Article;
import com.doodl6.wechatrobot.response.NewsMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.HttpUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
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
    public Object getTulingResponse(String content, String fromUserName, String toUserName) {
        String info = new String(content.getBytes(), StandardCharsets.UTF_8);
        String requestUrl = API_URL + "?key=" + appConfig.getApiKey() + "&info=" + info + "&userid=" + fromUserName;
        String result = HttpUtil.get(requestUrl);
        return processTuRingResult(result, toUserName, fromUserName);
    }

    /**
     * 处理图灵机器人返回的结果
     */
    private static Object processTuRingResult(String result, String fromUserName, String toUserName) {
        JSONObject rootObj = JSON.parseObject(result);
        int code = rootObj.getIntValue("code");
        if (TulingConstants.TEXT_CODE.equals(code)) {
            return new TextMessage(fromUserName, toUserName, rootObj.getString("text"));
        } else if (TulingConstants.LINK_CODE.equals(code)) {
            return new TextMessage(fromUserName, toUserName, "<a href='" + rootObj.getString("url") + "'>"
                    + rootObj.getString("text") + "</a>");
        } else if (TulingConstants.NEWS_CODE.equals(code) || TulingConstants.TRAIN_CODE.equals(code)
                || TulingConstants.FLIGHT_CODE.equals(code) || TulingConstants.MENU_CODE.equals(code)) {
            NewsMessage news = new NewsMessage(fromUserName, toUserName);
            List<JSONObject> list = JSON.parseArray(rootObj.getString("list"), JSONObject.class);
            assembleNews(news, list, code);
            return news;
        } else if (TulingConstants.LENGTH_WRONG_CODE.equals(code) || TulingConstants.KEY_WRONG_CODE.equals(code)) {
            return new TextMessage(fromUserName, toUserName, "我现在想一个人静一静,请等下再跟我聊天");
        } else if (TulingConstants.EMPTY_CONTENT_CODE.equals(code)) {
            return new TextMessage(fromUserName, toUserName, "你不说话,我也没什么好说的");
        } else if (TulingConstants.NUMBER_DONE_CODE.equals(code)) {
            return new TextMessage(fromUserName, toUserName, "我今天有点累了,明天再找我聊吧！");
        } else if (TulingConstants.NOT_SUPPORT_CODE.equals(code)) {
            return new TextMessage(fromUserName, toUserName, "这个我还没学会,我以后会慢慢学的");
        } else if (TulingConstants.UPGRADE_CODE.equals(code)) {
            return new TextMessage(fromUserName, toUserName, "我经验值满了,正在升级中...");
        } else if (TulingConstants.DATA_EXCEPTION_CODE.equals(code)) {
            return new TextMessage(fromUserName, toUserName, "你都干了些什么,我怎么话都说不清楚了");
        }
        return null;
    }

    /**
     * 根据消息类型组装图文消息
     */
    private static void assembleNews(NewsMessage news, List<JSONObject> list, int code) {
        String titleKey = "article";
        String descriptionKey = "article";
        if (TulingConstants.MENU_CODE.equals(code)) {
            titleKey = "name";
            descriptionKey = "info";
        }
        List<Article> articles = Lists.newLinkedList();
        for (JSONObject obj : list) {
            Article article = new Article();
            if (TulingConstants.TRAIN_CODE.equals(code)) {
                article.setTitle(obj.getString("trainnum") + " —— 开车时间:" + obj.getString("starttime"));
                article.setDescription(obj.getString("start") + "(" + obj.getString("starttime") + ")——>"
                        + obj.getString("terminal") + "(" + obj.getString("endtime") + ")");
            } else if (TulingConstants.FLIGHT_CODE.equals(code)) {
                article.setTitle(obj.getString("flight") + " —— 起飞时间:" + obj.getString("starttime"));
                article.setDescription(obj.getString("route") + obj.getString("starttime") + "——>"
                        + obj.getString("endtime") + "\n航班状态:" + obj.getString("state"));
            } else {
                article.setTitle(obj.getString(titleKey));
                article.setDescription(obj.getString(descriptionKey));
            }
            article.setPicUrl(obj.getString("icon"));
            article.setUrl(obj.getString("detailurl"));
            articles.add(article);
            if (articles.size() == 10) {
                break;
            }
        }
        news.setArticles(articles);
        news.setArticleCount(articles.size());
    }
}
