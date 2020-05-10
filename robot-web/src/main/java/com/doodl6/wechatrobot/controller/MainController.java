package com.doodl6.wechatrobot.controller;

import com.doodl6.wechatrobot.service.WeChatService;
import com.doodl6.wechatrobot.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/weChat")
@Slf4j
public class MainController {

    @Resource
    private WeChatService weChatService;

    /**
     * 接收来至微信服务器的消息
     **/
    @RequestMapping("receiveMessage")
    public String receiveMessage(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request, HttpServletResponse response) {
        String result;
        if (HttpMethod.GET.name().equals(request.getMethod())) {
            // 验证签名是否有效
            if (weChatService.checkSignature(signature, timestamp, nonce)) {
                result = echostr;
            } else {
                result = "你是谁？你想干嘛？";
            }
        } else {
            Map<String, String> parameters;
            try {
                parameters = XmlUtil.parseXml(request.getInputStream());
                result = weChatService.processReceived(parameters);
            } catch (Exception e) {
                log.error("获取来自微信的消息异常", e);
                result = StringUtils.EMPTY;
            }
        }

        return result;
    }

}
