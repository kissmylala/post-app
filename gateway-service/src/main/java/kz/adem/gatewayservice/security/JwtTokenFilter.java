package kz.adem.gatewayservice.security;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.gatewayservice.service.TokenValidationService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter implements WebFilter {
    private final RouterValidator routerValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenValidationService tokenValidationService;
    private final ReactiveUserDetailsService userDetailsService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (!routerValidator.isSecured.test(request)) {
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }
        String actualToken = token.substring(7);
        log.debug("Token: {}", actualToken);
        String tokenType = jwtTokenProvider.extractTokenType(actualToken);
        log.debug("Token type: {}", tokenType);
        if ("refresh_token".equals(tokenType)) {
            if (!request.getURI().getPath().equals("/api/auth/refresh-token")) {
                return Mono.error(new RuntimeException("Refresh token is not allowed"));
            }
            return processToken(exchange, chain, actualToken);
        }
        return tokenValidationService.isTokenValid(actualToken)
                .flatMap(isTokenValid -> {
                    log.debug("isTokenValid result: {}", isTokenValid);
                    boolean isTokenStructureValid = jwtTokenProvider.validateToken(actualToken);
                    log.debug("validateToken result: {}", isTokenStructureValid);
                    boolean isTokenNotExpired = !jwtTokenProvider.isTokenExpired(actualToken);
                    log.debug("isTokenNotExpired result: {}", isTokenNotExpired);
                    if (isTokenStructureValid && isTokenNotExpired && isTokenValid) {
                        return processToken(exchange, chain, actualToken);
                    } else {
                        return Mono.error(new RuntimeException("Token is not valid"));
                    }
                });
    }

    private Mono<Void> processToken(ServerWebExchange exchange, WebFilterChain chain, String actualToken) {
        String username = jwtTokenProvider.extractUsername(actualToken);
        String userId = jwtTokenProvider.extractUserId(actualToken);
        return userDetailsService.findByUsername(username)
                .flatMap(userDetails ->{
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("user", username)
                            .header("user_id", userId)
                            .build();
                    return chain.filter(exchange.mutate().request(mutatedRequest).build())
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
                });

    }


}