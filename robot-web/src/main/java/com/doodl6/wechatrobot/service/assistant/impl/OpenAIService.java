package com.doodl6.wechatrobot.service.assistant.impl;

import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.service.assistant.AssistantService;
import com.doodl6.wechatrobot.util.AddressUtil;
import com.doodl6.wechatrobot.util.PropertyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.List;

public class OpenAIService implements AssistantService {

    private static final String OPENAI_API_KEY = "OPENAI_API_KEY";

    private static final String OPENAI_BASE_DOMAIN = "OPENAI_BASE_DOMAIN";

    private static final String OPENAI_SERVER_URL = "OPENAI_SERVER_URL";

    private static final String OPENAI_PROXY = "OPENAI_PROXY";

    private static final String MODEL = "gpt-3.5-turbo";

    private static OpenAiService openAiService;

    public OpenAIService() {
        String apiKey = PropertyUtil.getProperty(OPENAI_API_KEY);
        if (StringUtils.isBlank(apiKey)) {
            return;
        }

        String proxyAddress = PropertyUtil.getProperty(OPENAI_PROXY);
        OkHttpClient.Builder clientBuilder = OpenAiService.defaultClient(apiKey, Duration.ofSeconds(10)).newBuilder();
        if (StringUtils.isNotBlank(proxyAddress)) {
            boolean valid = AddressUtil.validateAddress(proxyAddress);
            if (!valid) {
                throw new RuntimeException("OPENAI_PROXY is not valid, value:" + proxyAddress);
            }

            String[] parts = proxyAddress.split(":");
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(parts[0], parts.length == 2 ? Integer.parseInt(parts[1]) : 80));
            clientBuilder.proxy(proxy);
        }

        OkHttpClient client = clientBuilder.build();
        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        Retrofit.Builder retrofitBuilder = OpenAiService.defaultRetrofit(client, mapper).newBuilder();

        String serverUrl = PropertyUtil.getProperty(OPENAI_SERVER_URL);
        if (StringUtils.isNotBlank(serverUrl)) {
            retrofitBuilder.baseUrl(serverUrl);
        } else {
            String baseDomain = PropertyUtil.getProperty(OPENAI_BASE_DOMAIN);
            if (StringUtils.isNotBlank(baseDomain)) {
                boolean valid = AddressUtil.validateAddress(baseDomain);
                if (!valid) {
                    throw new RuntimeException("OPENAI_BASE_DOMAIN is not valid, value:" + baseDomain);
                }

                retrofitBuilder.baseUrl("https://" + baseDomain + "/");
            }
        }

        Retrofit retrofit = retrofitBuilder.build();
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        openAiService = new OpenAiService(api);
    }

    /**
     * 获取消息响应
     */
    @Override
    public BaseMessage processText(String content, String fromUserName) {
        if (openAiService == null) {
            return null;
        }

        ChatMessage systemMsg = new ChatMessage();
        systemMsg.setRole("system");
        systemMsg.setContent("你是一个AI助手，尽量保证回复内容在200个字符以内");

        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(content);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(MODEL)
                .messages(Lists.newArrayList(systemMsg, userMsg))
                .build();
        List<ChatCompletionChoice> choiceList = openAiService.createChatCompletion(chatCompletionRequest).getChoices();
        choiceList.forEach(System.out::println);
        String result = choiceList.get(0).getMessage().getContent();
        return new TextMessage(result);
    }
}
