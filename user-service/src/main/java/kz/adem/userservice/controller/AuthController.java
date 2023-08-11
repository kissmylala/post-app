package kz.adem.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.userservice.dto.LoginDto;
import kz.adem.userservice.dto.RegisterDto;
import kz.adem.userservice.security.JwtAuthResponse;
import kz.adem.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    //Build login Rest API
    @PostMapping(value = {"/login","/signin"})
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        JwtAuthResponse jwtAuthResponse = authService.login(loginDto);
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    //Build register Rest API
    @PostMapping(value = {"/register","/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @GetMapping("/refresh-token")
    public ResponseEntity<JwtAuthResponse> refreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        JwtAuthResponse jwtAuthResponse = authService.refreshToken(refreshToken);
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
}