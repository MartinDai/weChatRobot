package com.doodl6.wechatrobot.handle;

import com.doodl6.wechatrobot.config.KeywordConfig;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.service.TulingService;
import com.doodl6.wechatrobot.util.LogUtil;
import com.doodl6.wechatrobot.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 文本类型消息处理类
 */
@Service
@Slf4j
public class TextMessageHandle implements WeChatMessageHandle {

    @Resource
    private TulingService tulingService;

    @Resource
    private KeywordConfig keywordConfig;

    @Override
    public String processMessage(final Map<String, String> parameters) {

        log.info(LogUtil.buildLog("收到用户文本信息", parameters));

        String fromUserName = parameters.get("FromUserName");
        String toUserName = parameters.get("ToUserName");
        String content = parameters.get("Content");

        BaseMessage message = keywordConfig.getMessageByKeyword(content);
        if (message == null) {
            message = tulingService.getTulingResponse(content, fromUserName);
        }

        if (message != null) {
            message.setFromUserName(toUserName);
            message.setToUserName(fromUserName);
            message.setCreateTime(System.currentTimeMillis());
        }
        return MessageUtil.ObjectToXml(message);
    }
}
