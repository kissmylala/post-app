FROM openjdk:17

LABEL maintainer = "ademshanghai@gmail.com"

WORKDIR /app

COPY target/registry-service-0.0.1-SNAPSHOT.jar /app/registry-service.jar

ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar", "registry-service.jar"]