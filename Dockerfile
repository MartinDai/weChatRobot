FROM maven:3.8.4-eclipse-temurin-8 as build

COPY . /src/weChatRobot
WORKDIR /src/weChatRobot
RUN mvn package

FROM openjdk:8-jdk-oracle

USER root

COPY --from=build /src/weChatRobot/robot-web/target/weChatRobot.jar /weChatRobot/
COPY ./docker/start.sh /weChatRobot/

RUN chmod +x /weChatRobot/start.sh

EXPOSE 8080
WORKDIR /weChatRobot
ENTRYPOINT [ "sh", "start.sh" ]