package kz.adem.gatewayservice.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {
    public static final List<String> openEndpoints =
    List.of("/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/signin", "/api/v1/auth/signup","/api/token/validate"
    ,"/api/v1/users/liked/usernames","/api/v1/comments/**","/api/v1/like/comment","/api/v1/users/**","/swagger-ui/index.html");
    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
