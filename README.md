# weChatRobot
一个简单的智能聊天机器人项目，基于微信公众号和图灵机器人开发。

![qrcode](robot-web/src/main/webapp/images/qrcode.jpg "扫码关注，体验智能机器人")

## 功能介绍：
  本项目是一个微信公众号项目，需配合微信公众号使用，在微信公众号配置本项目运行的服务器域名，用户关注公众号后，向公众号发送任意信息，公众号会根据用户发送的内容自动回复。
  
## 涉及框架及技术
+ JDK 1.8
+ SpringBoot 2.2.6.RELEASE
+ FastJson
+ Log4j2 + Slf4j

## 使用说明：
1. 使用之前需要有微信公众号的帐号以及图灵机器人的帐号，没有的请戳[微信公众号申请](https://mp.weixin.qq.com/cgi-bin/readtemplate?t=register/step1_tmpl&lang=zh_CN)和[图灵机器人帐号注册](http://tuling123.com/register/email.jhtml),。
2. 在src/main/resources目录下的application.yml文件里面配置相关的key。
3. 微信公众号URL配置为`http://robot.doodl6.com/weChat/receiveMessage`,其中`robot.doodl6.com`是你自己的域名，token与`application.yml`文件配置一致即可。
4. 本地启动入口：com.doodl6.wechatrobot.WebStarter直接运行

## 支持的功能
* [x] 自动回复文本消息，回复内容来自于图灵机器人
* [ ] 自定义关键字回复内容
