package kz.adem.gatewayservice.security;

import kz.adem.gatewayservice.service.TokenValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


/**
 * A web filter that intercepts requests to validate JWT tokens.
 * It checks if the incoming request has a valid JWT token, determines its type (e.g., access or refresh token),
 * and processes the token accordingly. If the token is valid, the filter sets the user's authentication context
 * and forwards the request. If the token is invalid or missing, the filter stops the request from proceeding further.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter implements WebFilter {
    private final RouterValidator routerValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenValidationService tokenValidationService;
    private final ReactiveUserDetailsService userDetailsService;
    private static final String REFRESH_TOKEN_URL = "/api/v1/auth/refresh-token";
    private static final String USER_HEADER = "user";
    private static final String USER_ID_HEADER = "user_id";
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
            if (!request.getURI().getPath().equals(REFRESH_TOKEN_URL)) {
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
                            .header(USER_HEADER, username)
                            .header(USER_ID_HEADER, userId)
                            .build();
                    return chain.filter(exchange.mutate().request(mutatedRequest).build())
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
                });

    }


}