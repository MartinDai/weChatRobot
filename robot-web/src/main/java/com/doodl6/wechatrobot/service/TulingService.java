package com.doodl6.wechatrobot.service;

import com.doodl6.wechatrobot.config.AppConfig;
import com.doodl6.wechatrobot.constant.TulingConstants;
import com.doodl6.wechatrobot.domain.TulingTextReq;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.HttpUtil;
import com.doodl6.wechatrobot.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 图灵服务类
 */
@Slf4j
@Service
public class TulingService {

    private static final String API_URL = "http://openapi.turingapi.com/openapi/api/v2";

    @Resource
    private AppConfig appConfig;

    /**
     * 获取图灵机器人消息响应
     */
    public BaseMessage getTulingResponse(String content, String fromUserName) {
        String info = new String(content.getBytes(), StandardCharsets.UTF_8);
        TulingTextReq req = TulingTextReq.buildReq(appConfig.getApiKey(), fromUserName, info);
        String result = HttpUtil.post(API_URL, JsonUtil.objToJson(req));
        return processTuRingResult(result);
    }

    /**
     * 处理图灵机器人返回的结果
     */
    private static BaseMessage processTuRingResult(String result) {
        JsonNode rootNode = JsonUtil.jsonToObj(result, JsonNode.class);
        JsonNode intentNode = rootNode.get("intent");
        int code = intentNode.get("code").asInt();
        if (TulingConstants.PARAM_ERROR.equals(code)) {
            return new TextMessage("我不是很理解你说的话");
        } else if (TulingConstants.NO_API_TIMES.equals(code)) {
            return new TextMessage("我今天已经说了太多话了，有点累，明天再来找我聊天吧！");
        } else if (TulingConstants.NO_RESULTS.equals(code)) {
            return new TextMessage("我竟无言以对！");
        } else if (TulingConstants.SUCCESS.equals(code)) {
            if (TulingConstants.NOT_SUPPORT.equals(intentNode.get("operateState").asInt())) {
                return new TextMessage("暂不支持该功能");
            }

            ArrayNode resultsNode = (ArrayNode) rootNode.get("results");
            Iterator<JsonNode> resultNodeIterator = resultsNode.elements();
            StringBuilder resultBuilder = new StringBuilder();
            while (resultNodeIterator.hasNext()) {
                JsonNode resultNode = resultNodeIterator.next();
                String resultType = resultNode.get("resultType").asText();
                if ("text".equals(resultType)) {
                    resultBuilder.append(resultNode.get("values").get("text").asText()).append("\n");
                } else if ("url".equals(resultType)) {
                    String url = resultNode.get("values").get("url").asText();
                    resultBuilder.append("<a href='").append(url).append("'>").append(url).append("</a>\n");
                }
            }
            return new TextMessage(resultBuilder.toString());
        } else {
            return new TextMessage("我也不知道说什么好");
        }
    }

}
