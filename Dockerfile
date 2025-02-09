FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

COPY src src
COPY pom.xml pom.xml

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk

ENV APP_USER="exchangerate"

RUN adduser ${APP_USER} --shell /sbin/nologin

RUN mkdir -p /app && \
    chown -R ${APP_USER}:${APP_USER} /app

COPY --from=build --chown=$APP_USER target/exchangerate-*.jar /app/exchangerate.jar

USER $APP_USER
WORKDIR /app
ENTRYPOINT ["java","-jar","exchangerate.jar"]
