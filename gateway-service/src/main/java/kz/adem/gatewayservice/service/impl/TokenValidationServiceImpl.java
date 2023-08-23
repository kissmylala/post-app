package kz.adem.gatewayservice.service.impl;

import kz.adem.gatewayservice.repository.TokenRepository;
import kz.adem.gatewayservice.service.TokenValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {
    private final TokenRepository tokenRepository;

    @Override
    public Mono<Boolean> isTokenValid(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .defaultIfEmpty(false);
    }
}
