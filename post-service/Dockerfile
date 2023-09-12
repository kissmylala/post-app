FROM openjdk:17

LABEL maintainer = "ademshanghai@gmail.com"

WORKDIR /app

COPY target/post-service-0.0.1-SNAPSHOT.jar /app/post-service.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar", "post-service.jar"]