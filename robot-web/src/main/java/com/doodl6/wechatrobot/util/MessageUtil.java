package com.doodl6.wechatrobot.util;

import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.Writer;

/**
 * 消息工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageUtil {

    private static final XppDriver XPP_DRIVER;

    private static final XStream MESSAGE_PARSER;

    static {
        XPP_DRIVER = new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        //对所有xml节点的转换都增加CDATA标记
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }
                };
            }
        };

        MESSAGE_PARSER = new XStream();
        MESSAGE_PARSER.processAnnotations(WeChatMessage.class);
    }

    /**
     * 解析来自微信的消息
     */
    public static WeChatMessage parseWeChatMessage(InputStream inputStream) {
        return (WeChatMessage) MESSAGE_PARSER.fromXML(inputStream);
    }

    /**
     * 转换为响应xml
     */
    public static String toXml(Object obj) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }

        XStream xStream = new XStream(XPP_DRIVER);
        xStream.processAnnotations(obj.getClass());

        return xStream.toXML(obj);
    }

}
