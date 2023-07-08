package com.doodl6.wechatrobot.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XmlUtil {

    private static final Logger LOGGER = Logger.getLogger(XmlUtil.class.getName());

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    public static <T> T xmlToObj(String xml, Class<T> clazz) {
        try {
            return XML_MAPPER.readValue(xml, clazz);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "xml转clazz异常", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static String objToXml(Object obj) {
        try {
            return XML_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "obj转xml异常", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
