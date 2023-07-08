package com.doodl6.wechatrobot.processor;

import com.doodl6.wechatrobot.config.KeywordConfig;
import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.enums.WeChatMsgType;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.service.ChatGptService;
import com.doodl6.wechatrobot.service.KeywordService;
import com.doodl6.wechatrobot.service.TulingService;
import com.doodl6.wechatrobot.util.LogUtil;
import io.vertx.core.Vertx;

import java.util.logging.Logger;

/**
 * 文本类型消息处理类
 */
public class TextMessageProcessor implements WeChatMessageProcessor {

    private static final Logger LOGGER = Logger.getLogger(TextMessageProcessor.class.getName());

    private final KeywordService keywordService;

    private final ChatGptService chatGptService;

    private final TulingService tulingService;

    public TextMessageProcessor(Vertx vertx, KeywordConfig keywordConfig) {
        this.keywordService = new KeywordService(vertx, keywordConfig);
        this.chatGptService = new ChatGptService();
        this.tulingService = new TulingService();
    }

    @Override
    public WeChatMsgType getMsgType() {
        return WeChatMsgType.TEXT;
    }

    @Override
    public BaseMessage processMessage(WeChatMessage weChatMessage) {

        LOGGER.info(LogUtil.buildLog("收到用户文本信息", weChatMessage));

        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        String content = weChatMessage.getContent();

        //优先查找关键字配置
        BaseMessage message = keywordService.getMessageByKeyword(content);

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
