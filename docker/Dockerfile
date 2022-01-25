FROM maven:3.8.4-jdk-8-slim as builder

COPY . /src/weChatRobot
COPY ./docker/settings-docker.xml /usr/share/maven/ref/
WORKDIR /src/weChatRobot
RUN mvn package -s /usr/share/maven/ref/settings-docker.xml dependency:resolve

FROM openjdk:8-jre-slim-buster

USER root

COPY --from=builder /src/weChatRobot/robot-web/target/weChatRobot.jar /weChatRobot/
COPY ./docker/start.sh /weChatRobot/

RUN chmod +x /weChatRobot/start.sh

EXPOSE 8080
WORKDIR /weChatRobot
ENTRYPOINT [ "sh", "start.sh" ]