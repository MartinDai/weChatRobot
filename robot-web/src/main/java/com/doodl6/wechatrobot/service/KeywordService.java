package com.doodl6.wechatrobot.service;

import com.doodl6.wechatrobot.config.KeywordConfig;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.NewsMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.util.HttpUtil;
import com.doodl6.wechatrobot.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import io.vertx.core.Vertx;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 关键字服务类
 */
public class KeywordService {

    private static final Logger LOGGER = Logger.getLogger(KeywordService.class.getName());

    /**
     * 当前配置的版本
     */
    private static String currentConfigVersion;

    /**
     * 关键字回复内容配置
     */
    private static Map<String, JsonNode> keywordMessageMap = Maps.newHashMap();

    private final String versionLocation;

    private final String messageLocation;

    public KeywordService(Vertx vertx, KeywordConfig keywordConfig) {
        this.versionLocation = keywordConfig.getVersionLocation();
        this.messageLocation = keywordConfig.getMessageLocation();
        if (StringUtils.isNotBlank(this.versionLocation)) {
            if (versionLocation.startsWith("http")) {
                this.startReloadTask();
            } else {
                this.loadByResource(vertx);
            }
        }
    }

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

    public void startReloadTask() {
        scheduledExecutorService.scheduleAtFixedRate(new ReloadKeywordTask(), 0, 1, TimeUnit.MINUTES);
    }

    private class ReloadKeywordTask extends TimerTask {

        @Override
        public void run() {
            loadByHttp(versionLocation, messageLocation);
        }
    }

    private synchronized void loadByResource(Vertx vertx) {
        String version = vertx.fileSystem().readFileBlocking(versionLocation).toString(StandardCharsets.UTF_8);
        String messageConfigStr = vertx.fileSystem().readFileBlocking(messageLocation).toString(StandardCharsets.UTF_8);
        replaceKeywordMessageMap(version, messageConfigStr);
    }

    private synchronized void loadByHttp(String versionLocation, String messageLocation) {
        try {
            String newVersion = HttpUtil.get(versionLocation);

            if (Objects.equals(currentConfigVersion, newVersion)) {
                return;
            }

            String messageConfigStr = HttpUtil.get(messageLocation);
            replaceKeywordMessageMap(newVersion, messageConfigStr);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "根据Http请求获取关键字配置信息异常", e);
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
            LOGGER.log(Level.INFO,"初始化关键字map，{0} : {1}", new Object[]{keyword, messageJsonNode.toString()});
        }
        currentConfigVersion = newVersion;
        keywordMessageMap = newKeywordMessageMap;
    }
}
