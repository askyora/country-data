FROM yohanshanaka81/jre-base:0.0.1
WORKDIR /home/tmp
VOLUME /tmp
EXPOSE 8080
ADD target/*.jar app.jar
ENV JAVA_OPTS="-noverify -XX:+AlwaysPreTouch"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar"]