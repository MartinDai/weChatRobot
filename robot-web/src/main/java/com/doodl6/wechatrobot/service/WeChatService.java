package com.doodl6.wechatrobot.service;

import com.doodl6.wechatrobot.config.AppConfig;
import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.enums.WeChatMsgType;
import com.doodl6.wechatrobot.handle.WeChatMessageHandle;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微信服务类
 */
@Slf4j
@Service
public class WeChatService {

    @Resource
    private List<WeChatMessageHandle> weChatMessageHandleList;

    private Map<WeChatMsgType, WeChatMessageHandle> messageHandleMap;

    @Resource
    private AppConfig appConfig;

    @PostConstruct
    public void init() {
        messageHandleMap = weChatMessageHandleList.stream().collect(Collectors.toMap(WeChatMessageHandle::getMsgType, t -> t));
    }

    /**
     * 验证微信消息合法性
     */
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        if (signature == null || timestamp == null || nonce == null) {
            return false;
        }
        String[] arr = new String[]{appConfig.getToken(), timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (String str : arr) {
            content.append(str);
        }
        String tmpStr;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes(StandardCharsets.UTF_8));
            tmpStr = byteToHex(digest);
            return tmpStr.equals(signature);
        } catch (Exception e) {
            log.error("校验签名异常", e);
        }
        return false;
    }

    /**
     * 处理收到的消息
     */
    public BaseMessage processReceived(InputStream inputStream) {
        BaseMessage resultMessage;
        WeChatMessage weChatMessage = XmlUtil.xmlToObj(inputStream, WeChatMessage.class);
        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        try {
            WeChatMsgType msgType = WeChatMsgType.findByValue(weChatMessage.getMsgType());
            WeChatMessageHandle weChatMessageHandle = messageHandleMap.get(msgType);
            if (weChatMessageHandle == null) {
                resultMessage = new TextMessage(toUserName, fromUserName, "你说啥我咋没懂呢[疑问]");
            } else {
                resultMessage = weChatMessageHandle.processMessage(weChatMessage);
            }
        } catch (Exception e) {
            log.error("处理来至微信服务器的消息出现错误", e);
            resultMessage = new TextMessage(toUserName, fromUserName, "我竟无言以对！");
        }

        return resultMessage;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
