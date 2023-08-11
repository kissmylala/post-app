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
    private final GatewayFilter jwtTokenFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user_service_route", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.filter(jwtTokenFilter))
                        .uri("lb://USER-SERVICE"))
                .route("post_service_route", r -> r.path("/api/v1/posts/**")
                        .filters(f -> f.filter(jwtTokenFilter))
                        .uri("lb://POST-SERVICE"))
                .route("user_service_auth",r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(jwtTokenFilter))
                        .uri("lb://USER-SERVICE"))
                .route("user_service_admin",r -> r.path("/api/v1/admin/**")
                        .filters(f -> f.filter(jwtTokenFilter))
                        .uri("lb://USER-SERVICE"))
                .build();
    }



}
