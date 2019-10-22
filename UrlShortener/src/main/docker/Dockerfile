FROM openjdk:8-jre-alpine
COPY ./target/tinyurl-0.0.1-SNAPSHOT.jar /usr/src/docker/
WORKDIR /usr/src/docker
EXPOSE 8080
CMD ["java", "-jar", "tinyurl-0.0.1-SNAPSHOT.jar"]