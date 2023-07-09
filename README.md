# weChatRobot

一个基于微信公众号的智能聊天机器人项目，支持图灵机器人(V2)和ChatGPT对话模式回复内容

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
+ [x] 调用ChatGPT接口回复内容（需配置启动参数或者环境变量：`OPENAI_API_KEY`）
+ [x] 调用图灵机器人(V2)接口回复内容（需配置启动参数或者环境变量：`TULING_API_KEY`）

## 使用说明：

1. 使用之前需要有微信公众号的帐号，没有的请戳[微信公众号申请](https://mp.weixin.qq.com/cgi-bin/readtemplate?t=register/step1_tmpl&lang=zh_CN)
2. 如果需要使用图灵机器人的回复内容则需要[注册图灵机器人帐号](http://tuling123.com/register/email.jhtml)获取相应的ApiKey并配置在启动参数或者环境变量中
3. 如果需要使用ChatGPT的回复内容则需要[创建OpenAI的API Key](https://platform.openai.com/account/api-keys)并配置在启动参数或者环境变量中
4. 可以通过配置启动参数或者环境变量`OPENAI_BASE_DOMAIN`更换访问OpenAI的域名
5. 可以通过配置启动参数或者环境变量`OPENAI_PROXY`使用代理服务访问OpenAI
6. 内容响应来源的优先级`自定义关键 > ChatGPT > 图灵机器人`
7. 在微信公众号后台配置回调URL为<https://wechatrobot.doodl6.com/weChat/receiveMessage>，其中`wechatrobot.doodl6.com`是你自己的域名，token与`config.yml`里面配置的保持一致即可

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

### native-image运行

构建native-image(需要[graalvm版本的jdk](https://www.graalvm.org/downloads/))

```shell
mvn clean package -P native-image
```

构建完成后，在robot-web/target目录会生成weChatRobot可执行文件，可以直接运行

```shell
./weChatRobot
```

**注意：native-image不支持通过-D指定配置**

### Docker运行

构建适用于当前操作系统架构的镜像

```shell
docker build -f docker/Dockerfile --no-cache -t wechatrobot:latest .
```

构建指定架构的镜像

```shell
docker buildx build -f docker/Dockerfile --no-cache -t wechatrobot:latest --platform=linux/amd64 -o type=docker .
```

如果需要构建native-image的镜像，替换上面命令中的`docker/Dockerfile`为`docker/native-image-Dockerfile`即可

后台运行镜像

```shell
docker run --name wechatrobot -p 8080:8080 -d wechatrobot:latest
```

## 感谢赞助

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg" width="140" height="140" alt="jetbrains"/>](https://www.jetbrains.com/community/opensource/#support)