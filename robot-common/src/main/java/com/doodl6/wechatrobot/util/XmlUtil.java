package com.doodl6.wechatrobot.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class XmlUtil {

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    public static <T> T xmlToObj(InputStream inputStream, Class<T> clazz) {
        try {
            return XML_MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            log.error("xml格式化异常", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
