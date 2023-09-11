# Post-App

Post-App is a microservices-based project built with Spring Boot, allowing users to create, read, and comment on posts made by other users.

## Services

### Registry-service (Eureka Server)
Eureka Server provides service registration, allowing other services to discover and communicate with each other. It's a fundamental part of the Spring Cloud infrastructure, ensuring that services can scale and remain decoupled.

### Post-service
This service is responsible for handling operations related to posts.

### Comment-service
This service is dedicated to managing comments on posts.

### Gateway-service (Main API)
Gateway-service acts as the main API and router. It is responsible for user management, including authentication, authorization, registration, and login. It's built using Spring WebFlux.

## Inter-service Communication
Services communicate with each other using OpenFeign, which simplifies the HTTP API client by integrating directly with Spring Boot applications.

## Technology Stack
- Spring Boot
- Spring WebFlux (for the main API)
- Spring Security (based on JWT tokens)
- Spring Cloud
- Spring MVC

## Documentation
All controller and endpoint documentation can be accessed via Swagger:
- Gateway-service: [http://localhost:{port}/swagger-ui.html](http://localhost:{port}/swagger-ui.html)
- Post and Comment services: [http://localhost:{port}/swagger-ui/index.html](http://localhost:{port}/swagger-ui/index.html)

## Future Enhancements
- Addition of unit and integration tests.
- Docker containerization for easy deployment and scaling.

