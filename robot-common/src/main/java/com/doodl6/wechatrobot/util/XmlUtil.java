package com.doodl6.wechatrobot.util;

import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlUtil {

    private static XppDriver xppDriver = new XppDriver() {
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

    public static XStream getXstream() {
        return new XStream(xppDriver);
    }

    /**
     * 解析xml
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(InputStream inputStream) throws Exception {

        Map<String, String> map = Maps.newHashMap();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        //得到xml根元素
        Element root = document.getRootElement();
        //得到根元素的所有子节点
        List<Element> elementList = root.elements();
        //遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }
        //关闭流
        inputStream.close();

        return map;
    }

}
