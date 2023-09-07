package kz.adem.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user_service_route", r -> r.path("/api/v1/users/**")
                        .uri("lb://USER-SERVICE"))
                .route("post_service_route", r -> r.path("/api/v1/posts/**")
                        .uri("lb://POST-SERVICE"))
                .route("user_service_auth",r -> r.path("/api/auth/**")
                        .uri("lb://USER-SERVICE"))
                .route("user_service_admin",r -> r.path("/api/v1/admin/**")
                        .uri("lb://USER-SERVICE"))
                .route("post_like_service",r -> r.path("/api/v1/like/**")
                        .uri("lb://LIKE-SERVICE"))
                .route("post_comment_service",r -> r.path("/api/v1/comments/**")
                        .uri("lb://COMMENT-SERVICE"))
                .build();
    }



}
