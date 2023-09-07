package kz.adem.gatewayservice.service.impl;

import kz.adem.gatewayservice.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class LogoutService implements ServerLogoutHandler {
    private final TokenRepository tokenRepository;


    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        ServerHttpRequest request =  exchange.getExchange().getRequest();
        final String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }
        jwt = authHeader.substring(7);
        return tokenRepository.findByToken(jwt)
                .flatMap(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                    return tokenRepository.save(token);
                })
                .then(Mono.fromRunnable(() -> ReactiveSecurityContextHolder.clearContext()));
    }
}
