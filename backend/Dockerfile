FROM maven:3.9.6-eclipse-temurin-21 AS build
LABEL maintainer="danilodiniz.silva44@gmail.com"
WORKDIR /app
COPY target/Moto-2000-0.0.1-SNAPSHOT.jar /app/moto2000.jar
ENTRYPOINT ["java", "-jar", "moto2000.jar"]
EXPOSE 8080

