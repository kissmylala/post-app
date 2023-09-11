package kz.adem.gatewayservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication controller", description = "Register/sign in/refresh-token operations.")
public class AuthController {

    private final AuthService authService;


    //Build register Rest API
    @Operation(summary = "Register in the system")
    @ApiResponse(responseCode = "201", description = "Successfully registered!")
    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> register(@RequestBody @Validated RegisterDto registerDto){
        return authService.register(registerDto);

    }

    //Build login Rest API
    @Operation(summary = "Sign in using username and password")
    @ApiResponse(responseCode = "200", description = "Successfully logged in")
    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<JwtAuthResponse> login(@RequestBody @Validated LoginDto loginDto){
        return authService.login(loginDto);
    }

    //Build refresh token API
    @Operation(summary = "Get refresh token")
    @ApiResponse(responseCode = "200", description = "Successfully got refresh token")
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