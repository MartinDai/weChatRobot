package com.doodl6.wechatrobot.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebConfig {

    public AppConfig app = new AppConfig();

    public WechatConfig wechat = new WechatConfig();

    public KeywordConfig keyword = new KeywordConfig();

}
