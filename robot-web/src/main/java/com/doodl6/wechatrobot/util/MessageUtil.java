package com.doodl6.wechatrobot.util;

import com.doodl6.wechatrobot.response.Article;
import com.doodl6.wechatrobot.response.NewsMessage;
import com.thoughtworks.xstream.XStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 消息工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageUtil {

    /**
     * 对象转换成xml字符串
     */
    public static <T> String ObjectToXml(T t) {
        if (t == null) {
            return StringUtils.EMPTY;
        }
        XStream xStream = XmlUtil.getXstream();
        xStream.alias("xml", t.getClass());
        if (t instanceof NewsMessage) {
            xStream.alias("item", Article.class);
        }
        return xStream.toXML(t);
    }


}
