package kz.adem.userservice.service;

import kz.adem.userservice.dto.LoginDto;
import kz.adem.userservice.dto.RegisterDto;
import kz.adem.userservice.security.JwtAuthResponse;

public interface AuthService {

        JwtAuthResponse login(LoginDto loginDto);
        String register(RegisterDto registerDto);
        JwtAuthResponse refreshToken(String refreshToken);

}
