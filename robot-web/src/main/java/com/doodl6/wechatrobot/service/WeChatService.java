package com.doodl6.wechatrobot.service;

import com.doodl6.wechatrobot.config.AppConfig;
import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.enums.WeChatMsgType;
import com.doodl6.wechatrobot.handle.WeChatMessageHandle;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;

/**
 * 微信服务类
 */
@Service
@Slf4j
public class WeChatService {

    @Resource
    private WeChatMessageHandle eventMessageHandle;

    @Resource
    private WeChatMessageHandle textMessageHandle;

    @Resource
    private AppConfig appConfig;

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
    public String processReceived(InputStream inputStream) {
        String result;
        WeChatMessage weChatMessage = MessageUtil.parseWeChatMessage(inputStream);
        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        try {
            WeChatMsgType msgType = WeChatMsgType.findByValue(weChatMessage.getMsgType());
            if (msgType == WeChatMsgType.EVENT) {
                result = eventMessageHandle.processMessage(weChatMessage);
            } else if (msgType == WeChatMsgType.TEXT) {
                result = textMessageHandle.processMessage(weChatMessage);
            } else {
                result = MessageUtil.toXml(new TextMessage(toUserName, fromUserName, "我只对文字感兴趣[悠闲]"));
            }
        } catch (Exception e) {
            log.error("处理来至微信服务器的消息出现错误", e);
            result = MessageUtil.toXml(new TextMessage(toUserName,
                    fromUserName, "我竟无言以对！"));
        }

        return result;
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
