package com.doodl6.wechatrobot.handle;

import com.doodl6.wechatrobot.config.KeywordConfig;
import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.enums.WeChatMsgType;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.service.ChatGptService;
import com.doodl6.wechatrobot.service.TulingService;
import com.doodl6.wechatrobot.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 文本类型消息处理类
 */
@Service
@Slf4j
public class TextMessageHandle implements WeChatMessageHandle {

    @Autowired(required = false)
    private KeywordConfig keywordConfig;

    @Resource
    private ChatGptService chatGptService;

    @Resource
    private TulingService tulingService;

    @Override
    public WeChatMsgType getMsgType() {
        return WeChatMsgType.TEXT;
    }

    @Override
    public BaseMessage processMessage(WeChatMessage weChatMessage) {

        log.info(LogUtil.buildLog("收到用户文本信息", weChatMessage));

        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        String content = weChatMessage.getContent();

        //优先查找关键字配置
        BaseMessage message = null;
        if (keywordConfig != null) {
            message = keywordConfig.getMessageByKeyword(content);
        }

        //再尝试从GPT获取响应
        if (message == null) {
            message = chatGptService.getResponse(content);
        }

        //再尝试从图灵机器人获取响应
        if (message == null) {
            message = tulingService.getResponse(content, fromUserName);
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
