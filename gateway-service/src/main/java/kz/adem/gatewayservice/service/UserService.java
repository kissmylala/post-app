package kz.adem.gatewayservice.service;


import kz.adem.gatewayservice.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {
    Flux<UserDto> getAllUsers();
    Mono<UserDto> getUserByEmail(String email);
    Mono<UserDto> getUserById(Long id);
    Mono<UserDto> getUserByUsername(String username);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);
    Flux<UserDto> getAllByEnabledIsTrue();
    Flux<UserDto> getAllByEnabledIsFalse();
    Mono<UserDto> updateUser(Long userId, UserDto userDto);
    Mono<UserDto> createUser(UserDto userDto);
    Mono<UserDto> createAdmin(UserDto userDto);
    Mono<String> banUser(String username);
    Flux<String> getAllUsernamesByIdIn(List<Long> ids);



}
