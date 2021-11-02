FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1-alpine@sha256:961d26d00378688d5dd6bd4e00859f8fc9faaf33e2bf3cd528db8306e778287f
WORKDIR /home/tmp
VOLUME /tmp
EXPOSE 8080
ADD target/*.jar app.jar
ENV JAVA_OPTS="-noverify -XX:+AlwaysPreTouch"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar"]