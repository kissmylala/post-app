package kz.adem.gatewayservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter implements GatewayFilter {
    private final RouterValidator routerValidator;
    private final JwtTokenProvider jwtTokenProvider;
//    private final TokenServiceClient tokenServiceClient;

    private WebClient webClient = WebClient.builder().build();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (routerValidator.isSecured.test(request)) {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                String actualToken = token.substring(7);
                System.out.println(actualToken);
                String tokenType = jwtTokenProvider.extractTokenType(actualToken);
                System.out.println(tokenType);
                if ("refresh_token".equals(tokenType) && !request.getURI().getPath().equals("/api/auth/refresh-token")) {
                    return Mono.error(new RuntimeException("Refresh token is not allowed"));
                }
                if ("refresh_token".equals(tokenType) && request.getURI().getPath().equals("/api/auth/refresh-token")) {
                    String username = jwtTokenProvider.extractUsername(actualToken);
                    String userId = jwtTokenProvider.extractUserId(actualToken);
                    exchange.getRequest().mutate().header("user", username)
                            .header("user_id",userId)
                            .build();
                    return chain.filter(exchange);
                }
                return webClient.get()
                        .uri("http://localhost:8082/api/token/validate?token=" + actualToken)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .flatMap(isTokenValid -> {
                            if (jwtTokenProvider.validateToken(actualToken) && !jwtTokenProvider.isTokenExpired(actualToken) && isTokenValid) {
                                String username = jwtTokenProvider.extractUsername(actualToken);
                                String userId = jwtTokenProvider.extractUserId(actualToken);
                                exchange.getRequest().mutate().header("user", username)
                                        .header("user_id",userId)
                                        .build();
                                return chain.filter(exchange);
                            } else {
                                return Mono.error(new RuntimeException("Token is not valid"));
                            }
                        });
            }
        }
        return chain.filter(exchange);
    }
}




//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//        ServerHttpRequest request = exchange.getRequest();
//        if (routerValidator.isSecured.test(request)) {
//            String token = exchange.getRequest().getHeaders()
//                    .getFirst(HttpHeaders.AUTHORIZATION);
//            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
//                String actualToken = token.substring(7);
//                System.out.println(actualToken);
//                //write me method to extract token type
//                String tokenType = jwtTokenProvider.extractTokenType(actualToken);
//                System.out.println(tokenType);
//                if ("refresh_token".equals(tokenType) && !request.getURI().getPath().equals("/api/auth/refresh-token")) {
//                    throw new RuntimeException("Refresh token is not allowed");
//                }
//                Boolean isTokenValid = webClient.get()
//                        .uri("http://localhost:8082/api/token/validate?token=" + actualToken)
//                        .retrieve()
//                        .bodyToMono(Boolean.class)
//                        .block();
//                if (jwtTokenProvider.validateToken(actualToken) && !jwtTokenProvider.isTokenExpired(actualToken) && isTokenValid) {
//            String username = jwtTokenProvider.extractUsername(actualToken);
//            exchange.getRequest().mutate().header("user", username);
//            return chain.filter(exchange);
//
//            } else {
//                return Mono.error(new RuntimeException("Missing auth token"));
//            }
//        }
//
//    }
//        return chain.filter(exchange);}
//
//
//        }





//                return tokenServiceClient.isTokenValid(actualToken)
//            .flatMap(isTokenValid -> {
//        if (jwtTokenProvider.validateToken(actualToken) && !jwtTokenProvider.isTokenExpired(actualToken) && isTokenValid) {
//            String username = jwtTokenProvider.extractUsername(actualToken);
//            exchange.getRequest().mutate().header("user", username);
//            return chain.filter(exchange);
//        } else {
//            return Mono.error(new RuntimeException("Token is not valid"));
//        }
//    });
//}


//return tokenServiceClient.isTokenValid(actualToken)
//        .flatMap(isTokenValid -> {
//        if (jwtTokenProvider.validateToken(actualToken) && !jwtTokenProvider.isTokenExpired(actualToken) && isTokenValid) {
//        String username = jwtTokenProvider.extractUsername(actualToken);
//        exchange.getRequest().mutate().header("user", username);
//        return chain.filter(exchange);
//        } else {
//        return Mono.error(new RuntimeException("Token is not valid"));
//        }
//        });

//    private boolean authMissing (ServerHttpRequest request){
//        return !request.getHeaders().containsKey("Authorization");
//
//    }