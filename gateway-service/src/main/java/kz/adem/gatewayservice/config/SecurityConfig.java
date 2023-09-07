package kz.adem.gatewayservice.config;

import kz.adem.gatewayservice.security.JwtAuthenticationEntryPoint;
import kz.adem.gatewayservice.security.JwtTokenFilter;
import kz.adem.gatewayservice.service.impl.LogoutService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private LogoutService logoutService;
    private JwtTokenFilter tokenFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler((exchange, denied) -> {
                    exchange.getResponse().setStatusCode( HttpStatus.FORBIDDEN);
                    return Mono.empty();
                }))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/auth/login", "/api/auth/register", "/api/auth/logout", "/api/token/**", "/api/v1/like/**", "/api/v1/comments/**", "/api/v1/users/liked/usernames"
                        ,"/api/v1/users/liked/users").permitAll()
                        .pathMatchers(HttpMethod.GET,"/api/v1/users/**").permitAll()
                        .anyExchange().authenticated())
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .addFilterBefore(tokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)

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