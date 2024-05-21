package com.doodl6.wechatrobot.processor;

import com.doodl6.wechatrobot.config.KeywordConfig;
import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.enums.WeChatMsgType;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.service.assistant.AssistantService;
import com.doodl6.wechatrobot.service.assistant.impl.DashscopeService;
import com.doodl6.wechatrobot.service.assistant.impl.KeywordService;
import com.doodl6.wechatrobot.service.assistant.impl.OpenAIService;
import com.doodl6.wechatrobot.service.assistant.impl.TulingService;
import com.doodl6.wechatrobot.util.JsonUtil;
import com.google.common.collect.Lists;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 文本类型消息处理类
 */
@Slf4j
public class TextMessageProcessor implements WeChatMessageProcessor {

    private final List<AssistantService> assistantServiceList;

    public TextMessageProcessor(Vertx vertx, KeywordConfig keywordConfig) {
        this.assistantServiceList = Lists.newLinkedList();
        assistantServiceList.add(new KeywordService(vertx, keywordConfig));
        assistantServiceList.add(new OpenAIService());
        assistantServiceList.add(new DashscopeService());
        assistantServiceList.add(new TulingService());
    }

    @Override
    public WeChatMsgType getMsgType() {
        return WeChatMsgType.TEXT;
    }

    @Override
    public BaseMessage processMessage(WeChatMessage weChatMessage) {

        log.info("收到用户文本信息: {}", JsonUtil.objToJson(weChatMessage));

        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        String content = weChatMessage.getContent();
        BaseMessage message = null;
        for (AssistantService assistantService : assistantServiceList) {
            message = assistantService.processText(content, fromUserName);
            if (message != null) {
                break;
            }
        }

        //最后返回收到的文本信息作为兜底
        if (message == null) {
            message = new TextMessage(toUserName, fromUserName, content);
        } else {
            message.setFromUserName(toUserName);
            message.setToUserName(fromUserName);
            message.setCreateTime(System.currentTimeMillis());
        }

        return message;
    }
}
