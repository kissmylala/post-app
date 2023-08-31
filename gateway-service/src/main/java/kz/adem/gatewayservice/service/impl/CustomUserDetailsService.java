package kz.adem.gatewayservice.service.impl;

import kz.adem.gatewayservice.repository.RoleRepository;
import kz.adem.gatewayservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with username: "+username)))
                .flatMap(user -> roleRepository.findById(user.getRoleId())
                        .map(role -> {
                            Set<GrantedAuthority> authorities = new HashSet<>();
                            authorities.add(new SimpleGrantedAuthority(role.getName()));
                            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);
                        }));

    }
}
