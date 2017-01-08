package com.mandy.wechatrobot.controller;

import com.mandy.wechatrobot.handle.WeChatMessageHandle;
import com.mandy.wechatrobot.util.Constants;
import com.mandy.wechatrobot.util.MessageUtil;
import com.mandy.wechatrobot.util.WeChatUtil;
import com.mandy.wechatrobot.vo.TextMessage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

@Controller
@RequestMapping("/weChat")
public class MainController {

    private static Logger logger = Logger.getLogger(MainController.class);

    @Resource
    private WeChatMessageHandle eventMessageHandle;

    @Resource
    private WeChatMessageHandle textMessageHandle;

    /**
     * 接收来至微信服务器的消息
     **/
    @RequestMapping("receiveMessage")
    public void receiveMessage(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        String fromUserName = null;
        String toUserName = null;
        try {
            out = response.getWriter();
            if (Constants.GET.equals(request.getMethod())) {
                // 验证签名是否有效
                if (WeChatUtil.checkSignature(signature, timestamp, nonce)) {
                    out.write(echostr);
                } else {
                    out.write("");
                }
            } else {
                String result;
                Map<String, String> parameters = MessageUtil.parseXml(request
                        .getInputStream());
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
                out.write(result);
            }
        } catch (Exception e) {
            logger.error("接收来至微信服务器的消息出现错误", e);
            out.write(MessageUtil.ObjectToXml(new TextMessage(toUserName,
                    fromUserName, "我竟无言以对！")));
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
