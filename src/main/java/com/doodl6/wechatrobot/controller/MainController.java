package com.doodl6.wechatrobot.controller;

import com.alibaba.fastjson.util.IOUtils;
import com.doodl6.wechatrobot.handle.WeChatMessageHandle;
import com.doodl6.wechatrobot.util.Constants;
import com.doodl6.wechatrobot.util.MessageUtil;
import com.doodl6.wechatrobot.util.WeChatUtil;
import com.doodl6.wechatrobot.vo.TextMessage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

@Controller
@RequestMapping("/weChat")
public class MainController {

    private static Logger LOGGER = Logger.getLogger(MainController.class);

    @Resource
    private WeChatMessageHandle eventMessageHandle;

    @Resource
    private WeChatMessageHandle textMessageHandle;

    /**
     * 接收来至微信服务器的消息
     **/
    @RequestMapping("receiveMessage")
    public void receiveMessage(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request, HttpServletResponse response) {
        String fromUserName = null;
        String toUserName = null;
        try {
            if (Constants.GET.equals(request.getMethod())) {
                // 验证签名是否有效
                if (WeChatUtil.checkSignature(signature, timestamp, nonce)) {
                    writeText(echostr, response);
                } else {
                    writeText("你是谁？你想干嘛？", response);
                }
            } else {
                String result;
                Map<String, String> parameters = MessageUtil.parseXml(request.getInputStream());
                fromUserName = parameters.get("FromUserName");
                toUserName = parameters.get("ToUserName");
                String msgType = parameters.get("MsgType");
                if ("event".equals(msgType)) {
                    result = eventMessageHandle.processMessage(parameters);
                } else if ("text".equals(msgType)) {
                    result = textMessageHandle.processMessage(parameters);
                } else {
                    result = MessageUtil.ObjectToXml(new TextMessage(toUserName, fromUserName, "我只对文字感兴趣[悠闲]"));
                }

                writeText(result, response);
            }
        } catch (Exception e) {
            LOGGER.error("接收来至微信服务器的消息出现错误", e);
            writeText(MessageUtil.ObjectToXml(new TextMessage(toUserName,
                    fromUserName, "我竟无言以对！")), response);
        }

    }

    private void writeText(String content, HttpServletResponse response) {
        Writer writer = null;
        try {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            writer = response.getWriter();
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("响应客户端文本内容出现异常", e);
        } finally {
            IOUtils.close(writer);
        }
    }

}
