package kz.adem.gatewayservice.config;

import kz.adem.gatewayservice.security.JwtAuthenticationEntryPoint;
import kz.adem.gatewayservice.service.impl.LogoutService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private LogoutService logoutService;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler((exchange, denied) -> {
                    exchange.getResponse().setStatusCode( HttpStatus.FORBIDDEN);
                    return Mono.empty();
                }))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .logout(logoutSpec -> logoutSpec.logoutUrl("/api/auth/logout").logoutHandler(logoutService)
                        .logoutSuccessHandler(((exchange, authentication) -> Mono.fromRunnable(ReactiveSecurityContextHolder::clearContext))));
        return http.build();
    }
    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService){
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }





    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}