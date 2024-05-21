package com.doodl6.wechatrobot.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class XmlUtil {

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    public static <T> T xmlToObj(String xml, Class<T> clazz) {
        try {
            return XML_MAPPER.readValue(xml, clazz);
        } catch (IOException e) {
            log.error("xml转clazz异常", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static String objToXml(Object obj) {
        try {
            return XML_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("obj转xml异常", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
