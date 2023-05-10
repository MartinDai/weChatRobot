package com.doodl6.wechatrobot.service;

import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class ChatGptService {

    private static final String PROPERTIES_KEY = "OPENAI_API_KEY";

    private static final String MODEL = "gpt-3.5-turbo";

    private static OpenAiService openAiService;

    @PostConstruct
    public void init() {
        String apiKey = System.getProperty(PROPERTIES_KEY);
        if (apiKey == null) {
            apiKey = System.getenv(PROPERTIES_KEY);
        }

        if (StringUtils.isNotBlank(apiKey)) {
            ObjectMapper mapper = OpenAiService.defaultObjectMapper();
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
            OkHttpClient client = OpenAiService.defaultClient(apiKey, Duration.ofSeconds(10))
                    .newBuilder()
//                    .proxy(proxy)
                    .build();
            Retrofit retrofit = OpenAiService.defaultRetrofit(client, mapper);
            OpenAiApi api = retrofit.create(OpenAiApi.class);
            openAiService = new OpenAiService(api);
        }
    }

    /**
     * 获取消息响应
     */
    public BaseMessage getResponse(String content) {
        if (openAiService == null) {
            return null;
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("user");
        chatMessage.setContent(content);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(MODEL)
                .messages(Collections.singletonList(chatMessage))
                .build();
        List<ChatCompletionChoice> choiceList = openAiService.createChatCompletion(chatCompletionRequest).getChoices();
        choiceList.forEach(System.out::println);
        String result = choiceList.get(0).getMessage().getContent();
        return new TextMessage(result);
    }
}
