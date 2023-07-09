FROM maven:3.8.4-jdk-8-slim as builder

COPY . /src/weChatRobot
COPY ./docker/settings-docker.xml /usr/share/maven/ref/
WORKDIR /src/weChatRobot
RUN mvn package -s /usr/share/maven/ref/settings-docker.xml

FROM openjdk:8-alpine

USER root

COPY --from=builder /src/weChatRobot/robot-web/target/weChatRobot.jar /app/
COPY ./docker/start.sh /weChatRobot/

RUN chmod +x /weChatRobot/start.sh

EXPOSE 8080
WORKDIR /app
ENTRYPOINT [ "sh", "start.sh" ]