package com.doodl6.wechatrobot.service;

import com.doodl6.wechatrobot.constant.TulingConstants;
import com.doodl6.wechatrobot.domain.TulingTextReq;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.HttpUtil;
import com.doodl6.wechatrobot.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 图灵服务类
 */
public class TulingService {

    private static final String API_URL = "http://openapi.turingapi.com/openapi/api/v2";

    private static final String PROPERTIES_KEY = "TULING_API_KEY";

    private static String API_KEY;

    public TulingService() {
        API_KEY = System.getProperty(PROPERTIES_KEY);
        if (API_KEY == null) {
            API_KEY = System.getenv(PROPERTIES_KEY);
        }
    }

    /**
     * 获取消息响应
     */
    public BaseMessage getResponse(String content, String fromUserName) {
        if (StringUtils.isBlank(API_KEY)) {
            return null;
        }

        String info = new String(content.getBytes(), StandardCharsets.UTF_8);
        TulingTextReq req = TulingTextReq.buildReq(API_KEY, fromUserName, info);
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
