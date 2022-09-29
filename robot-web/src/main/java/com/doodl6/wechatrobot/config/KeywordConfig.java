package com.doodl6.wechatrobot.config;

import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.NewsMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.HttpUtil;
import com.doodl6.wechatrobot.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 关键字配置
 */
@Slf4j
@Configuration
@ConfigurationProperties("keyword.location")
@ConditionalOnProperty(prefix = "keyword.location", name = "version")
public class KeywordConfig implements ApplicationContextAware, DisposableBean {

    /**
     * 当前配置的版本
     */
    private static String currentConfigVersion;

    /**
     * 关键字回复内容配置
     */
    private static Map<String, JsonNode> keywordMessageMap = Maps.newHashMap();

    private ApplicationContext applicationContext;

    @Setter
    private String version;

    @Setter
    private String message;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("reload-keyword-task");
        return thread;
    });

    public BaseMessage getMessageByKeyword(String keyword) {
        JsonNode messageJsonNode = keywordMessageMap.get(keyword);
        if (messageJsonNode == null) {
            return null;
        }

        String type = messageJsonNode.get("type").asText();
        BaseMessage baseMessage = null;
        if ("text".equals(type)) {
            baseMessage = JsonUtil.jsonToObj(messageJsonNode.toString(), TextMessage.class);
        } else if ("news".equals(type)) {
            baseMessage = JsonUtil.jsonToObj(messageJsonNode.toString(), NewsMessage.class);
        }
        return baseMessage;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void startReloadThread() {
        scheduledExecutorService.scheduleAtFixedRate(new ReloadKeywordTask(applicationContext, version, message), 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void destroy() {
        scheduledExecutorService.shutdown();
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
            String newVersion = HttpUtil.get(versionLocation);

            if (Objects.equals(currentConfigVersion, newVersion)) {
                return;
            }

            String messageConfigStr = HttpUtil.get(messageLocation);
            replaceKeywordMessageMap(newVersion, messageConfigStr);

        } catch (Exception e) {
            log.error("根据Http请求获取关键字配置信息异常", e);
        }
    }

    private static void replaceKeywordMessageMap(String newVersion, String messageConfigStr) {
        Map<String, JsonNode> newKeywordMessageMap = Maps.newHashMap();
        JsonNode messageConfigJson = JsonUtil.jsonToObj(messageConfigStr, JsonNode.class);
        Iterator<Map.Entry<String, JsonNode>> keywordMessageIterator = messageConfigJson.fields();
        while (keywordMessageIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = keywordMessageIterator.next();
            String keyword = entry.getKey();
            JsonNode messageJsonNode = entry.getValue();
            newKeywordMessageMap.put(keyword, messageJsonNode);
        }
        currentConfigVersion = newVersion;
        keywordMessageMap = newKeywordMessageMap;
    }
}
