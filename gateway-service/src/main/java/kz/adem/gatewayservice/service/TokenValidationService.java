package kz.adem.gatewayservice.service;

import reactor.core.publisher.Mono;

public interface TokenValidationService {
    Mono<Boolean> isTokenValid(String token);
}
