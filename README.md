# weChatRobot
一个简单的智能聊天机器人项目，基于微信公众号和图灵机器人开发。

##功能介绍：
  本项目是一个微信公众号项目，需配合微信公众号使用，在微信公众号配置本项目运行的服务器域名，用户关注公众号后，可回复任意信息，公众号会根据自动用户发送内容自动回复（回复内容来源于图灵机器人）。
  
##框架和工具
+ Java版本&IDE：JDK1.6+Intellij IDEA
+ 后端框架：springMVC3.2.4.RELEASE
+ Web容器：Jetty7.6.21.v20160908
+ 构建工具：maven
+ 其他：FastJson,Log4j

##使用说明：
1. 使用之前需要有微信公众号的帐号以及图灵机器人的帐号，没有的请戳[微信公众号申请](https://mp.weixin.qq.com/cgi-bin/readtemplate?t=register/step1_tmpl&lang=zh_CN)和[图灵机器人帐号注册](http://tuling123.com/register/email.jhtml),。
2. 在src/main/resources目录下的config.properties文件里面配置相关的key。
3. 发布到SAE的话需要把项目打包成war文件，然后删除xml-apis-1.0.b2.jar文件，这个包与SAE运行环境冲突。
4. 微信公众号URL配置为`http://robot.mandydai.com/weChat/receiveMessage.do`,其中`robot.mandydai.com`是你自己的域名，token与`app.properties`文件配置一致即可。
