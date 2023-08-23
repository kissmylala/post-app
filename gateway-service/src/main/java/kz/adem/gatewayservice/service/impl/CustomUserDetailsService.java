package kz.adem.gatewayservice.service.impl;

import kz.adem.gatewayservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with username: "+username)))
                .map(user -> {
                    Set<GrantedAuthority> authorities = user.getRoles().stream()
                            .map((role)-> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toSet());
                    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);
                });
    }
}
