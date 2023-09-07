package kz.adem.gatewayservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        logger.error("Authentication error: {}", ex.getMessage(), ex); // Логирование ошибки аутентификации

        return Mono.fromRunnable(()-> {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("WWW-Authenticate", "Bearer");
        });
    }
}

//@Component
//public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
//    @Override
//    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
//        return Mono.fromRunnable(()-> {
//            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
//            exchange.getResponse().getHeaders().add("WWW-Authenticate", "Bearer");
//        });
//
//    }
//}
