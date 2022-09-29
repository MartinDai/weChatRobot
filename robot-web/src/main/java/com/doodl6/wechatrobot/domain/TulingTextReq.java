package com.doodl6.wechatrobot.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TulingTextReq {

    private Integer reqType;

    private Perception perception;

    private UserInfo userInfo;

    public static TulingTextReq buildReq(String apiKey, String userId, String text) {
        TulingTextReq req = new TulingTextReq();
        //0表示文本消息
        req.setReqType(0);
        Perception perception = new Perception();
        InputText inputText = new InputText();
        inputText.setText(text);
        perception.setInputText(inputText);
        req.setPerception(perception);

        UserInfo userInfo = new UserInfo();
        userInfo.setApiKey(apiKey);
        userInfo.setUserId(userId);
        req.setUserInfo(userInfo);

        return req;
    }

    @Getter
    @Setter
    public static class Perception {

        private InputText inputText;
    }

    @Getter
    @Setter
    public static class InputText {

        private String text;
    }

    @Getter
    @Setter
    public static class UserInfo {

        private String apiKey;

        private String userId;
    }
}
