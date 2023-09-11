package kz.adem.gatewayservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Handles authentication failures in the application by responding with an HTTP 401 Unauthorized status.
 * This component acts as an entry point for JWT-based authentication, logging the authentication error
 * and setting the appropriate headers and status code in the response.
 */

@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        logger.error("Authentication error: {}", ex.getMessage(), ex); // logging auth error
        return Mono.fromRunnable(()-> {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("WWW-Authenticate", "Bearer");
        });
    }
}


