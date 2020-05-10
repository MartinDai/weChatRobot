package com.doodl6.wechatrobot.handle;

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

    @Override
    public String processMessage(final Map<String, String> parameters) {

        log.info(LogUtil.buildLog("收到用户文本信息", parameters));

        String fromUserName = parameters.get("FromUserName");
        String toUserName = parameters.get("ToUserName");
        String content = parameters.get("Content");
        Object obj = tulingService.getTulingResponse(content, fromUserName, toUserName);
        return MessageUtil.ObjectToXml(obj);
    }
}
