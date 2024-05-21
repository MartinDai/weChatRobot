package com.doodl6.wechatrobot.service.assistant.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.ApiKey;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.service.assistant.AssistantService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DashscopeService implements AssistantService {

    private boolean enable;

    public DashscopeService() {
        try {
            ApiKey.getApiKey(null);
            this.enable = true;
        } catch (NoApiKeyException ignore) {
        }
    }

    @Override
    public BaseMessage processText(String content, String fromUserName) {
        if (!enable) {
            return null;
        }

        try {
            Generation gen = new Generation();
            Message systemMsg = Message.builder().role(Role.SYSTEM.getValue()).content("你是一个AI助手，保持回复内容尽量简短").build();
            Message userMsg = Message.builder().role(Role.USER.getValue()).content(content).build();
            GenerationParam param =
                    GenerationParam.builder().model(Generation.Models.QWEN_TURBO).messages(Lists.newArrayList(systemMsg, userMsg))
                            .resultFormat(GenerationParam.ResultFormat.TEXT)
                            .build();
            GenerationResult result = gen.call(param);
            String responseText = result.getOutput().getText();
            if (responseText != null) {
                return new TextMessage(responseText);
            }
        } catch (Exception e) {
            log.error("调用通义千问接口异常", e);
        }

        return null;
    }
}
