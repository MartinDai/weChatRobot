# weChatRobot
一个简单的智能聊天机器人项目，基于微信公众号和图灵机器人开发。

##功能介绍：
  本项目是一个微信公众号项目，需配合微信公众号使用，在微信公众号配置本项目运行的服务器域名，用户关注公众号后，可回复任意信息，公众号会根据自动用户发送内容自动回复（回复内容来源于图灵机器人）。
  
##  项目介绍
1. 项目使用maven构建
2. 使用Jetty7.6.9.v20130131作为Web容器
3. JDK版本为1.6
4. 集成struts2.3.16和spring3.0.5.RELEASE版本的框架(不过没有做过多的配置，只有必须的简单配置)

##使用说明：
1. 使用之前需要有微信公众号的帐号以及图灵机器人的帐号，没有的请戳[微信公众号申请](https://mp.weixin.qq.com/cgi-bin/readtemplate?t=register/step1_tmpl&lang=zh_CN),[图灵机器人帐号注册](http://www.tuling123.com/openapi/cloud/register.jsp)。
2. 在src/main/resources目录下的config.properties文件里面配置相关的key。
3. 项目必须运行在公网上或者有内网映射。
4. 运行项目，并在微信公众号开发者设置里面配置域名相关信息。
5. 关注微信公众号，回复任意内容就可以查看效果啦！
