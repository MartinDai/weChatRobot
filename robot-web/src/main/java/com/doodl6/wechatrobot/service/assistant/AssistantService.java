package com.doodl6.wechatrobot.service.assistant;

import com.doodl6.wechatrobot.response.BaseMessage;

public interface AssistantService {

  BaseMessage processText(String content, String fromUserName);

}
