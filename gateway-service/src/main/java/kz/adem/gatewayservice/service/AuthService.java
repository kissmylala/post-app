package kz.adem.gatewayservice.service;

import kz.adem.gatewayservice.dto.JwtAuthResponse;
import kz.adem.gatewayservice.dto.LoginDto;
import kz.adem.gatewayservice.dto.RegisterDto;
import reactor.core.publisher.Mono;

public interface AuthService {

        Mono<JwtAuthResponse> login(LoginDto loginDto);
        Mono<String> register(RegisterDto registerDto);
        Mono<JwtAuthResponse> refreshToken(String refreshToken);

}
