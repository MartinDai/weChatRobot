package com.mandy.wechatrobot.handle;

import com.mandy.wechatrobot.constant.AppConstants;
import com.mandy.wechatrobot.util.Constants;
import com.mandy.wechatrobot.util.HttpUtil;
import com.mandy.wechatrobot.util.MessageUtil;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Map;

/**
 * 处理文本类型消息
 */
@Service
public class TextMessageHandle implements WeChatMessageHandle {

    @Override
    public String processMessage(final Map<String, String> parameters)
            throws Exception {
        String fromUserName = parameters.get("FromUserName");
        String toUserName = parameters.get("ToUserName");
        String content = parameters.get("Content");
        String info = URLEncoder.encode(content, "utf-8");
        String requestUrl = Constants.TURING_API_URL + "?key=" + AppConstants.API_KEY
                + "&info=" + info + "&userid=" + fromUserName;
        String result = HttpUtil.get(requestUrl);
        Object obj = MessageUtil.processTuRingResult(result, toUserName,
                fromUserName);
        return MessageUtil.ObjectToXml(obj);
    }
}
