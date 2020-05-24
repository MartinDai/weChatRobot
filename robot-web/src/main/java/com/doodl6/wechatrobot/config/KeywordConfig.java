package com.doodl6.wechatrobot.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.NewsMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.google.common.collect.Maps;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 关键字配置
 */
@Slf4j
@Configuration
@ConfigurationProperties("keyword.location")
public class KeywordConfig implements ApplicationContextAware {

    private static final HttpClient httpClient = HttpClientBuilder.create().build();

    /**
     * 当前配置的版本
     */
    private static String currentConfigVersion;

    /**
     * 关键字回复内容配置
     */
    private static Map<String, JSONObject> keywordMessageMap = Maps.newHashMap();

    private ApplicationContext applicationContext;

    @Setter
    private String version;

    @Setter
    private String message;

    private volatile boolean started;

    private Timer timer;

    public BaseMessage getMessageByKeyword(String keyword) {
        JSONObject messageJSON = keywordMessageMap.get(keyword);
        if (messageJSON == null) {
            return null;
        }

        String type = messageJSON.getString("type");
        BaseMessage baseMessage = null;
        if ("text".equals(type)) {
            baseMessage = messageJSON.toJavaObject(TextMessage.class);
        } else if ("news".equals(type)) {
            baseMessage = messageJSON.toJavaObject(NewsMessage.class);
        }
        return baseMessage;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void startReloadThread() {
        if (started) {
            return;
        }

        synchronized (this) {
            if (started) {
                return;
            }
            started = true;
        }
        timer = new Timer("reload-keyword-timer");
        timer.schedule(new ReloadKeywordTask(applicationContext, version, message), 0, 60000);
    }

    private static class ReloadKeywordTask extends TimerTask {

        private final ApplicationContext applicationContext;

        private final String versionLocation;

        private final String messageLocation;

        public ReloadKeywordTask(ApplicationContext applicationContext, String versionLocation, String messageLocation) {
            this.applicationContext = applicationContext;
            this.versionLocation = versionLocation;
            this.messageLocation = messageLocation;
        }

        @Override
        public void run() {
            if (versionLocation.startsWith("classpath:")) {
                reloadByResource(applicationContext, versionLocation, messageLocation);
            } else if (versionLocation.startsWith("http")) {
                reloadByHttp(versionLocation, messageLocation);
            }
        }
    }

    private static void reloadByResource(ApplicationContext applicationContext, String versionLocation, String messageLocation) {
        Resource versionResource = applicationContext.getResource(versionLocation);
        String newVersion = null;
        try (InputStream inputStream = versionResource.getInputStream()) {
            newVersion = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            if (Objects.equals(currentConfigVersion, newVersion)) {
                return;
            }
        } catch (Exception e) {
            log.error("检查关键字版本异常", e);
        }

        Resource messageResource = applicationContext.getResource(messageLocation);
        try (InputStream inputStream = messageResource.getInputStream()) {
            String messageConfigStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            replaceKeywordMessageMap(newVersion, messageConfigStr);
        } catch (IOException e) {
            log.error("加载关键字配置异常", e);
        }
    }

    private static void reloadByHttp(String versionLocation, String messageLocation) {
        try {
            HttpResponse versionResponse = httpClient.execute(new HttpGet(versionLocation));
            String newVersion = IOUtils.toString(versionResponse.getEntity().getContent(), StandardCharsets.UTF_8);

            if (Objects.equals(currentConfigVersion, newVersion)) {
                return;
            }

            HttpResponse messageResponse = httpClient.execute(new HttpGet(messageLocation));
            String messageConfigStr = IOUtils.toString(messageResponse.getEntity().getContent(), StandardCharsets.UTF_8);

            replaceKeywordMessageMap(newVersion, messageConfigStr);

        } catch (Exception e) {
            log.error("根据Http请求获取关键字配置信息异常", e);
        }
    }

    private static void replaceKeywordMessageMap(String newVersion, String messageConfigStr) {
        Map<String, JSONObject> newKeywordMessageMap = Maps.newHashMap();
        JSONObject messageConfigJSON = JSON.parseObject(messageConfigStr);
        for (String keyword : messageConfigJSON.keySet()) {
            JSONObject messageJSON = messageConfigJSON.getJSONObject(keyword);
            newKeywordMessageMap.put(keyword, messageJSON);
        }
        currentConfigVersion = newVersion;
        keywordMessageMap = newKeywordMessageMap;
    }
}
