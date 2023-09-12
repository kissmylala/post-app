FROM openjdk:17

LABEL maintainer = "ademshanghai@gmail.com"

WORKDIR /app

COPY target/gateway-service-0.0.1-SNAPSHOT.jar /app/gateway-service.jar

ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar", "gateway-service.jar"]