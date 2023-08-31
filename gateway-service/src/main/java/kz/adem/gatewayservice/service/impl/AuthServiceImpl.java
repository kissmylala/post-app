package kz.adem.gatewayservice.service.impl;

import kz.adem.gatewayservice.dto.JwtAuthResponse;
import kz.adem.gatewayservice.dto.LoginDto;
import kz.adem.gatewayservice.dto.RegisterDto;
import kz.adem.gatewayservice.entity.Token;
import kz.adem.gatewayservice.entity.User;
import kz.adem.gatewayservice.exception.BlogAPIException;
import kz.adem.gatewayservice.exception.ResourceNotFoundException;
import kz.adem.gatewayservice.repository.RoleRepository;
import kz.adem.gatewayservice.repository.TokenRepository;
import kz.adem.gatewayservice.repository.UserRepository;
import kz.adem.gatewayservice.security.JwtTokenProvider;
import kz.adem.gatewayservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.Date;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {


    private ReactiveAuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;
    private TokenRepository tokenRepository;
    private ReactiveUserDetailsService userDetailsService;
    private static final Long ROLE_USER = 2L;

    @Override
    public Mono<JwtAuthResponse> login(LoginDto loginDto) {
        return userRepository.findByUsername(loginDto.getUsername())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User", "username", loginDto.getUsername())))
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new BlogAPIException(HttpStatus.BAD_REQUEST, "Your account is banned!"));
                    }
                    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()))
                            .flatMap(authentication -> {

                                ReactiveSecurityContextHolder.withAuthentication(authentication);
                                String accessToken = tokenProvider.generateToken(authentication, user.getId());
                                String refreshToken = tokenProvider.generateRefreshToken(authentication, user.getId());
                                saveUserToken(user, accessToken);
                                return Mono.just(JwtAuthResponse.builder()
                                        .accessToken(accessToken)
                                        .refreshToken(refreshToken)
                                        .build());
                            });
                });
    }

    @Override
    public Mono<String> register(RegisterDto registerDto) {
        //add check for username exists in db
        return userRepository.existsByUsername(registerDto.getUsername())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BlogAPIException(HttpStatus.BAD_REQUEST, "Username already exists!"));
                    }
                    return userRepository.existsByEmail(registerDto.getEmail());
                })
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BlogAPIException(HttpStatus.BAD_REQUEST, "Email already exists!"));
                    }
                    return roleRepository.findByName("ROLE_USER");
                })
//                .switchIfEmpty(Mono.error(new BlogAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not set")))
                .flatMap(role -> {
                    User newUser = User.builder()
                            .name(registerDto.getName())
                            .username(registerDto.getUsername())
                            .email(registerDto.getEmail())
                            .password(passwordEncoder.encode(registerDto.getPassword()))
                            .roleId(ROLE_USER)
                            .createdAt(new Timestamp(new Date().getTime()))
                            .enabled(true)
                            .build();

                    return userRepository.save(newUser);
                })
                .thenReturn("User registered successfully!");
    }

    private void saveUserToken(User user, String token) {
        Token userToken = Token.builder()
                .userId(user.getId())
                .token(token)
                .tokenType("BEARER")
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(userToken).subscribe();
    }

    private void revokeAllUsersToken(User user) {
        tokenRepository.findAllValidTokenByUser(user.getId())
                .doOnNext(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                })
                .flatMap(tokenRepository::save)
                .subscribe();
    }

    @Override
    public Mono<JwtAuthResponse> refreshToken(String refreshToken) {
        return Mono.just(refreshToken)
                .filter(tokenProvider::validateToken)
                .switchIfEmpty(Mono.error(new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid refresh token")))
                .flatMap(validToken -> {
                    String username = tokenProvider.extractUsername(refreshToken);
                    return userRepository.findByUsername(username)
                            .switchIfEmpty(Mono.error(new ResourceNotFoundException("User", "username", username)))
                            .flatMap(user -> userDetailsService.findByUsername(username)
                                    .flatMap(userDetails -> ReactiveSecurityContextHolder.getContext()
                                            .flatMap(securityContext -> {
                                                System.out.println("securityContext.getAuthentication() = " + securityContext.getAuthentication());
                                                if (tokenProvider.isTokenValid(refreshToken, userDetails)) {
                                                    revokeAllUsersToken(user);
                                                    String newAccessToken = tokenProvider.generateToken(securityContext.getAuthentication(), user.getId());
                                                    System.out.println("newAccessToken = " + newAccessToken);
                                                    String newRefreshToken = tokenProvider.generateRefreshToken(securityContext.getAuthentication(), user.getId());
                                                    System.out.println("newRefreshToken = " + newRefreshToken);
                                                    saveUserToken(user, newAccessToken);
                                                    return Mono.just(JwtAuthResponse.builder()
                                                            .accessToken(newAccessToken)
                                                            .refreshToken(newRefreshToken)
                                                            .build());
                                                } else {
                                                    return Mono.error(new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid refresh token"));
                                                }
                                            })));
                });
    }



}