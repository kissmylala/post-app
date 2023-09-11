package kz.adem.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    //Routing configuration for services. Defines paths and their corresponding services.
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("post_service_route", r -> r.path("/api/v1/posts/**")
                        .uri("lb://POST-SERVICE"))

                .route("post_comment_service",r -> r.path("/api/v1/comments/**")
                        .uri("lb://COMMENT-SERVICE"))
                .build();
    }



}
