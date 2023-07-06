# weChatRobot

一个基于微信公众号的智能聊天机器人项目，支持图灵机器人(V2)和ChatGPT对话模式回复内容

本项目还有Go实现的版本：https://github.com/MartinDai/weChatRobot-go

![qrcode](robot-web/src/main/resources/static/images/qrcode.jpg "扫码关注，体验智能机器人")

## 项目介绍：

  本项目是一个微信公众号项目，需配合微信公众号使用，在微信公众号配置本项目运行的服务器域名，用户关注公众号后，向公众号发送任意信息，公众号会根据用户发送的内容自动回复。
  
## 涉及框架及技术

- [SpringBoot](https://github.com/spring-projects/spring-boot)
- [Jackson](https://github.com/FasterXML/jackson)
- [Logback](https://github.com/qos-ch/logback)
- [OkHttp](https://github.com/square/okhttp)
- [Guava](https://github.com/google/guava)
- [Openai-java](https://github.com/TheoKanning/openai-java)

## 支持的功能

+ [x] 自定义关键字回复内容
+ [x] 调用ChatGPT接口回复内容（需配置启动参数或者环境变量：`OPENAI_API_KEY`）
+ [x] 调用图灵机器人(V2)接口回复内容（需配置启动参数或者环境变量：`TULING_API_KEY`）

## 使用说明：

1. 使用之前需要有微信公众号的帐号，没有的请戳[微信公众号申请](https://mp.weixin.qq.com/cgi-bin/readtemplate?t=register/step1_tmpl&lang=zh_CN)
2. 如果需要使用图灵机器人的回复内容则需要[注册图灵机器人帐号](http://tuling123.com/register/email.jhtml)获取相应的ApiKey并配置在启动参数或者环境变量中
3. 如果需要使用ChatGPT的回复内容则需要[创建OpenAI的API Key](https://platform.openai.com/account/api-keys)并配置在启动参数或者环境变量中
4. 可以通过配置启动参数或者环境变量`OPENAI_BASE_DOMAIN`更换访问OpenAI的域名
5. 可以通过配置启动参数或者环境变量`OPENAI_PROXY`使用代理服务访问OpenAI
6. 内容响应来源的优先级`自定义关键 > ChatGPT > 图灵机器人`
7. 在微信公众号后台配置回调URL为[http://robot.doodl6.com/weChat/receiveMessage](http://robot.doodl6.com/weChat/receiveMessage)，其中`robot.doodl6.com`是你自己的域名，token与`config.yml`里面配置的保持一致即可

## 开发部署

### 本地启动

直接运行类`com.doodl6.wechatrobot.WebStarter`

### jar包运行

maven编译打包

```
mvn clean package
```

如果需要激活deploy这个profile，可以使用
```
mvn clean package -P deploy
```

打包完成后，在robot-web/target目录会生成weChatRobot.jar

启动执行

```shell
java -jar weChatRobot.jar
```

服务器部署后台运行

```shell
nohup java -jar weChatRobot.jar > ./console.log 2>&1 &
```

在执行命令的当前目录查看console日志以及logs目录查看业务日志

### Docker运行

执行下面这行命令可以得到一个编译好的镜像
```
docker build -f docker/Dockerfile --no-cache -t wechatrobot:latest .
```

编译好镜像以后，执行下面的命令，可以后台启动项目
```
docker run --name wechatrobot -p 8080:8080 -d wechatrobot:latest
```

## 感谢赞助

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg" width="140" height="140" alt="jetbrains"/>](https://www.jetbrains.com/community/opensource/#support)