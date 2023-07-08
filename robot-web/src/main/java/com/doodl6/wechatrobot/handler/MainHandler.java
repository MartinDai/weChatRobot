package com.doodl6.wechatrobot.handler;

import com.doodl6.wechatrobot.config.KeywordConfig;
import com.doodl6.wechatrobot.config.WechatConfig;
import com.doodl6.wechatrobot.processor.EventMessageProcessor;
import com.doodl6.wechatrobot.processor.TextMessageProcessor;
import com.doodl6.wechatrobot.processor.WeChatMessageProcessor;
import com.doodl6.wechatrobot.service.KeywordService;
import com.doodl6.wechatrobot.service.WeChatService;
import com.doodl6.wechatrobot.util.XmlUtil;
import com.google.common.collect.Lists;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = Logger.getLogger(KeywordService.class.getName());

    private final WeChatService weChatService;

    public MainHandler(Vertx vertx, WechatConfig wechatConfig, KeywordConfig keywordConfig) {
        List<WeChatMessageProcessor> processorList = Lists.newLinkedList();
        processorList.add(new TextMessageProcessor(vertx, keywordConfig));
        processorList.add(new EventMessageProcessor());
        weChatService = new WeChatService(wechatConfig, processorList);
    }

    @Override
    public void handle(RoutingContext context) {
        HttpServerRequest request = context.request();
        if (request.method() == HttpMethod.GET) {
            MultiMap queryParams = context.queryParams();
            String signature = queryParams.get("signature");
            String timestamp = queryParams.get("timestamp");
            String nonce = queryParams.get("nonce");
            // 验证签名是否有效
            if (weChatService.checkSignature(signature, timestamp, nonce)) {
                this.responseText(context, queryParams.get("echostr"));
            } else {
                this.responseText(context, "你是谁？你想干嘛？");
            }
        } else {
            request.bodyHandler(body -> {
                String requestBody = body.toString();
                Object result;
                try {
                    result = weChatService.processReceived(requestBody);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "获取来自微信的消息异常", e);
                    result = StringUtils.EMPTY;
                }
                this.responseXml(context, result);
            });

        }
    }

    private void responseText(RoutingContext context, String content) {
        context.response()
                .putHeader("content-type", "text/html;charset=utf8")
                .end(content);
    }

    private void responseXml(RoutingContext context, Object result) {
        context.response()
                .putHeader("content-type", "text/xml;charset=utf8")
                .end(XmlUtil.objToXml(result));
    }
}
