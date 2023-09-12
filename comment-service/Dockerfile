FROM openjdk:17

LABEL maintainer = "ademshanghai@gmail.com"

WORKDIR /app

COPY target/comment-service-0.0.1-SNAPSHOT.jar /app/comment-service.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar", "comment-service.jar"]