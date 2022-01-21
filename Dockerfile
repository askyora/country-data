FROM askyora/base-jre:11
WORKDIR /tmp
VOLUME /tmp
EXPOSE 8080
ADD target/country-data-jvm-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS="-noverify -XX:+AlwaysPreTouch"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /tmp/app.jar"]