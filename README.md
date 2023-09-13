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
- PostgreSQL
- Docker Compose


---

## Launch application

To launch the Post-App services using Docker, follow the steps below:

1. **Pre-requisites**:
    - Make sure you have [Docker](https://www.docker.com/get-started) and [Docker Compose](https://docs.docker.com/compose/install/) installed on your machine.
    - Clone the repository to your local machine.

      ```bash
      git clone https://github.com/kissmylala/post-app.git
      ```

2. **Navigate to the Project Directory**:

    ```bash
    cd post-app
    ```

3. **Build & Start the Services**:

   Using Docker Compose, you can build and start all services with a single command.

    ```bash
    docker-compose up --build
    ```

   > 📌 Note: The `--build` flag ensures that Docker images for the services are built if they haven't been already.

4. **Access the Application**:

   Once the services are up and running, you can access the application on the ports defined in the `docker-compose.yml` file. For example, if the Gateway-service runs on port `9191`, you can access it via [http://localhost:9191/](http://localhost:9191/).

5. **Stopping the Services**:

   When you're done, you can stop the services by pressing `Ctrl + C` in the terminal where `docker-compose` is running. To stop the services and also remove the containers, you can run:

    ```bash
    docker-compose down
    ```

---



## Documentation
All controller and endpoint documentation can be accessed via Swagger:
- Gateway-service: [http://localhost:{port}/swagger-ui.html](http://localhost:{port}/swagger-ui.html)
- Post and Comment services: [http://localhost:{port}/swagger-ui/index.html](http://localhost:{port}/swagger-ui/index.html)

## Future Enhancements
- Addition of unit and integration tests.


