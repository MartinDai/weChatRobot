# weChatRobot
一个简单的智能聊天机器人项目，基于微信公众号和图灵机器人(V1)开发。

本项目还有Go实现的版本：https://github.com/MartinDai/weChatRobot-go

![qrcode](robot-web/src/main/resources/static/images/qrcode.jpg "扫码关注，体验智能机器人")

## 项目介绍：
  本项目是一个微信公众号项目，需配合微信公众号使用，在微信公众号配置本项目运行的服务器域名，用户关注公众号后，向公众号发送任意信息，公众号会根据用户发送的内容自动回复。
  
## 涉及框架及技术
+ Jdk 1.8
+ SpringBoot
+ Jackson
+ Logback + Slf4j
+ OkHttp

## 支持的功能
* [x] 自动回复文本消息，回复内容来自于图灵机器人
* [x] 自定义关键字回复内容

## 使用说明：
1. 使用之前需要有微信公众号的帐号以及图灵机器人的帐号，没有的请戳[微信公众号申请](https://mp.weixin.qq.com/cgi-bin/readtemplate?t=register/step1_tmpl&lang=zh_CN)和[图灵机器人帐号注册](http://tuling123.com/register/email.jhtml)。
2. 在src/main/resources目录下的application.yml文件里面配置相关的key。
3. 微信公众号URL配置为`http://robot.doodl6.com/weChat/receiveMessage`,其中`robot.doodl6.com`是你自己的域名，token与`application.yml`文件配置一致即可。

## 开发部署

### 本地启动
直接运行类`com.doodl6.wechatrobot.WebStarter`

### 本地jar包运行
本地打包得到weChatRobot.jar这个文件，使用命令`java -jar weChatRobot.jar`即可运行，

### 服务器jar包部署
使用使用命令`mvn clean install -P deploy`打包得到jar文件，上传到服务器后使用命令`nohup java -jar weChatRobot.jar > ./console.log 2>&1 &`在后台运行，在执行命令的当前目录查看console日志以及logs目录查看业务日志

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

<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg" width="140" height="140" alt="jetbrains"/>

https://jb.gg/OpenSourceSupport


