package kz.adem.gatewayservice;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
//@EnableFeignClients
public class GatewayServiceApplication {

//    private final GatewayFilter jwtTokenFilter;

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}
//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route("user_service_route", r -> r.path("/api/v1/users/**")
//						.filters(f -> f.filter(jwtTokenFilter))
//						.uri("lb://USER-SERVICE"))
//				.route("post_service_route", r -> r.path("/api/v1/posts/**")
//						.filters(f -> f.filter(jwtTokenFilter))
//						.uri("lb://POST-SERVICE"))
//				.route("user_service_auth",r -> r.path("/api/auth/**")
//						.filters(f -> f.filter(jwtTokenFilter))
//						.uri("lb://USER-SERVICE"))
//				.route("user_service_admin",r -> r.path("/api/v1/admin/**")
//						.filters(f -> f.filter(jwtTokenFilter))
//						.uri("lb://USER-SERVICE"))
//				.build();
//	}
}
