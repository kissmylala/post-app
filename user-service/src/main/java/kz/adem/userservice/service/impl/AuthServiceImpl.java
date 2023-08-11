package kz.adem.userservice.service.impl;

import kz.adem.userservice.dto.LoginDto;
import kz.adem.userservice.dto.RegisterDto;
import kz.adem.userservice.entity.Role;
import kz.adem.userservice.entity.Token;
import kz.adem.userservice.entity.User;
import kz.adem.userservice.exception.BlogAPIException;
import kz.adem.userservice.exception.ResourceNotFoundException;
import kz.adem.userservice.repository.RoleRepository;
import kz.adem.userservice.repository.TokenRepository;
import kz.adem.userservice.repository.UserRepository;
import kz.adem.userservice.security.JwtAuthResponse;
import kz.adem.userservice.security.JwtTokenProvider;
import kz.adem.userservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

    @Service
    @AllArgsConstructor
    public class AuthServiceImpl implements AuthService {
        private AuthenticationManager authenticationManager;
        private UserRepository userRepository;
        private RoleRepository roleRepository;
        private PasswordEncoder passwordEncoder;
        private JwtTokenProvider tokenProvider;
        private TokenRepository tokenRepository;
        private UserDetailsService userDetailsService;
        @Override
        public JwtAuthResponse login(LoginDto loginDto) {
            User user = userRepository.findByUsername(loginDto.getUsername()).
                    orElseThrow(() -> new ResourceNotFoundException("User", "username", loginDto.getUsername()));
            if (!user.isEnabled()) {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Your account is banned!");
            }
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            revokeAllUsersToken(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(authentication);

            saveUserToken(user, accessToken);
            return JwtAuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        @Override
        public String register(RegisterDto registerDto) {
            //add check for username exists in db
            if (userRepository.existsByUsername(registerDto.getUsername())){
                throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Username already exists!");
            }
            if ( userRepository.existsByEmail(registerDto.getEmail())){
                throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Email already exists!");
            }
            Set<Role> roles = new HashSet<>();
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(()->new BlogAPIException(HttpStatus.INTERNAL_SERVER_ERROR,"Default role not set"));
            roles.add(defaultRole);
            User newUser = User.builder()
                    .name(registerDto.getName())
                    .username(registerDto.getUsername())
                    .email(registerDto.getEmail())
                    .password(passwordEncoder.encode(registerDto.getPassword()))
                    .roles(roles)
                    .createdAt(new Date())
                    .enabled(true)
                    .build();
            userRepository.save(newUser);
            return "User registered successfully!";
        }
        private void saveUserToken(User user,String token){
            Token userToken = Token.builder()
                    .user(user)
                    .token(token)
                    .tokenType("BEARER")
                    .expired(false)
                    .revoked(false)
                    .build();
            tokenRepository.save(userToken);
        }
        private void revokeAllUsersToken(User user){
            List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
            if (validUserTokens.isEmpty()){
                return;
            }
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
        public JwtAuthResponse refreshToken(String refreshToken){
             if (!tokenProvider.validateToken(refreshToken)){
                 throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Invalid refresh token");
                }
                String username = tokenProvider.extractUsername(refreshToken);
                User user = userRepository.findByUsername(username)
                      .orElseThrow(()->new ResourceNotFoundException("User","username",username));
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (tokenProvider.isTokenValid(refreshToken,userDetails)){
                    revokeAllUsersToken(user);
                    String newAccessToken = tokenProvider.generateToken(SecurityContextHolder.getContext().getAuthentication());
                    String newRefreshToken = tokenProvider.generateRefreshToken(SecurityContextHolder.getContext().getAuthentication());
                    saveUserToken(user,newAccessToken);
                    return JwtAuthResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(newRefreshToken)
                            .build();
                }
                throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Invalid refresh token");
        }
    }