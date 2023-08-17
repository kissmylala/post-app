package kz.adem.gatewayservice.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {
    public static final List<String> openEndpoints =
    List.of("/api/auth/login", "/api/auth/register", "/api/auth/signin", "/api/auth/signup","/api/token/validate","http://localhost:8082/api/token/validate"
    ,"/api/v1/like","/api/v1/users/liked/usernames","/api/v1/comments/**","/api/v1/like/comment");
    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
