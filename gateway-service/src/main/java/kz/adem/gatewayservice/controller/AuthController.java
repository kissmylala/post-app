package kz.adem.gatewayservice.controller;

import jakarta.servlet.http.HttpServletRequest;

import kz.adem.gatewayservice.dto.JwtAuthResponse;
import kz.adem.gatewayservice.dto.LoginDto;
import kz.adem.gatewayservice.dto.RegisterDto;
import kz.adem.gatewayservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    //Build login Rest API
    @PostMapping(value = {"/login","/signin"})
    @ResponseStatus(HttpStatus.OK)
    public Mono<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }

    //Build register Rest API
    @PostMapping(value = {"/register","/signup"})
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> register(@RequestBody RegisterDto registerDto){
        return authService.register(registerDto);

    }
    @GetMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Mono<JwtAuthResponse> refreshToken(ServerWebExchange request){
        String refreshToken = request.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        return authService.refreshToken(refreshToken);
    }
}