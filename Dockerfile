FROM openjdk:21-jdk

ENV APP_USER="exchangerate"

RUN adduser ${APP_USER} --shell /sbin/nologin

RUN mkdir -p /app && \
    chown -R ${APP_USER}:${APP_USER} /app

COPY --chown=$APP_USER target/exchangerate-*.jar /app/exchangerate.jar

USER $APP_USER
WORKDIR /app
ENTRYPOINT ["java","-jar","exchangerate.jar"]
