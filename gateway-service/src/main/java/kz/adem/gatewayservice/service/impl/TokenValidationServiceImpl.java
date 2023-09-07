package kz.adem.gatewayservice.service.impl;

import kz.adem.gatewayservice.entity.Token;
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
        System.out.println("Validating token: " + token); // Логируем токен

        return tokenRepository.findByToken(token)
                .doOnNext(t -> System.out.println("Found token in DB: " + t.toString())) // Логируем найденный токен
                .map(t -> {
                    boolean isValid = !t.isExpired() && !t.isRevoked();
                    System.out.println(isValid);
                    System.out.println("Token isExpired: " + t.isExpired() + ", isRevoked: " + t.isRevoked()); // Логируем значения
                    return isValid;
                })
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("Token not found in DB"); // Логируем, если токен не найден
                    return Mono.just(false);
                }));
    }



}
