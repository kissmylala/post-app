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
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    //Build login Rest API
    @PostMapping(value = {"/login","/signin"})
    public Mono<ResponseEntity<JwtAuthResponse>> login(@RequestBody LoginDto loginDto){
        return authService.login(loginDto)
                .map(ResponseEntity::ok);
    }

    //Build register Rest API
    @PostMapping(value = {"/register","/signup"})
    public Mono<ResponseEntity<String>> register(@RequestBody RegisterDto registerDto){
        return authService.register(registerDto)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));

    }
    @GetMapping("/refresh-token")
    public Mono<ResponseEntity<JwtAuthResponse>> refreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        return authService.refreshToken(refreshToken)
                .map(ResponseEntity::ok);
    }
}