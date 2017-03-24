FROM openjdk:8-jre-alpine

RUN apk add --no-cache tzdata bash

ARG mainClass
ENV mainClass $mainClass

ENV TZ Asia/Taipei

ADD libs /app/libs
ADD app /app

ENTRYPOINT java -cp /app/libs/*:/app/* ${mainClass}


