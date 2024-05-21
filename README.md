# weChatRobot

一个基于微信公众号的智能聊天机器人项目，支持根据关键字或者调用OpenAI、通义千问等大语言模型服务回复内容。

[![License](https://img.shields.io/github/license/martindai/wechatRobot)](LICENSE)

本项目还有Go实现的版本：<https://github.com/MartinDai/weChatRobot-go>

![qrcode](robot-web/src/main/resources/static/images/qrcode.jpg "扫码关注，体验智能机器人")

## 项目介绍：

本项目是一个微信公众号项目，需配合微信公众号使用，在微信公众号配置本项目运行的服务器域名，用户关注公众号后，向公众号发送任意信息，公众号会根据用户发送的内容自动回复。
  
## 涉及框架及技术

- [Vert.x](https://github.com/eclipse-vertx/vert.x)
- [Jackson](https://github.com/FasterXML/jackson)
- [OkHttp](https://github.com/square/okhttp)
- [Guava](https://github.com/google/guava)
- [Openai-java](https://github.com/TheoKanning/openai-java)

_Tips:1.2版本开始使用Vert.x替换SpringBoot_

## 支持的功能

+ [x] 自定义关键字回复内容
+ [x] 调用OpenAI接口回复内容（配置启动参数或者环境变量：`OPENAI_API_KEY`）
+ [x] 修改OpenAI的接口地址（配置启动参数或者环境变量：`OPENAI_SERVER_URL`）
+ [x] 调用通义千问接口回复内容（配置启动参数或者环境变量：`DASHSCOPE_API_KEY`）
+ [x] 调用图灵机器人(V2)接口回复内容（配置启动参数或者环境变量：`TULING_API_KEY`）

## 使用说明

需要有微信公众号的帐号，没有的请戳[微信公众号申请](https://mp.weixin.qq.com/cgi-bin/readtemplate?t=register/step1_tmpl&lang=zh_CN)

内容响应来源的优先级`自定义关键字 > OpenAI > 通义千问 > 图灵机器人`

在微信公众号后台配置回调URL为<https://<your.domain>/weChat/receiveMessage>，其中`<your.domain>`替换成你自己的域名，token与`config.yml`里面配置的保持一致即可

### OpenAI

1. 如果需要使用OpenAI的回复内容则需要[创建OpenAI的API Key](https://platform.openai.com/account/api-keys)并配置在启动参数或者环境变量中
2. 可以通过配置启动参数或者环境变量`OPENAI_SERVER_URL`指定访问OpenAI服务的baseUrl
3. 可以通过配置启动参数或者环境变量`OPENAI_BASE_DOMAIN`更换访问OpenAI的域名(优先级低于`OPENAI_SERVER_URL`)
4. 可以通过配置启动参数或者环境变量`OPENAI_PROXY`使用代理服务访问OpenAI

### 通义千问

如果需要使用通义千问的回复内容则需要[创建通义千问的API Key](https://bailian.console.aliyun.com/#/api_key)并配置在启动参数或者环境变量中

### 图灵机器人

如果需要使用图灵机器人的回复内容则需要[注册图灵机器人帐号](http://tuling123.com/register/email.jhtml)获取相应的ApiKey并配置在启动参数或者环境变量中

## 开发部署

### 本地启动

直接运行类`com.doodl6.wechatrobot.MainVerticle`

### jar包运行

maven编译打包

```shell
mvn clean package
```

打包完成后，在robot-web/target目录会生成weChatRobot.jar

启动执行

```shell
java -jar weChatRobot.jar
```

使用-D指定配置文件，支持相对路径和绝对路径

```shell
java -Dconfig=config-deploy.yml -jar weChatRobot.jar
```

服务器部署后台运行

```shell
nohup java -jar weChatRobot.jar > ./console.log 2>&1 &
```

在执行命令的当前目录查看console日志

### Docker运行

构建适用于当前操作系统架构的镜像

```shell
docker build -f docker/Dockerfile --no-cache -t wechatrobot:latest .
```

构建指定架构的镜像

```shell
docker buildx build -f docker/Dockerfile --no-cache -t wechatrobot:latest --platform=linux/amd64 -o type=docker .
```

后台运行镜像

```shell
docker run --name wechatrobot -p 8080:8080 -d wechatrobot:latest
```

## 感谢赞助

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg" width="140" height="140" alt="jetbrains"/>](https://www.jetbrains.com/community/opensource/#support)
