package kz.adem.gatewayservice.repository;

import kz.adem.gatewayservice.entity.User;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository extends R2dbcRepository<User,Long> {
    Mono<User> findByEmail(String email);
    Mono<User> findByUsernameOrEmail(String username, String email);
    Mono<User> findByUsername(String username);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);
    Flux<User> findAllByEnabledIsTrue();
    Flux<User> findAllByEnabledIsFalse();
    Mono<Long> findIdByUsername(String username);
    Flux<User> findAllByIdIn(List<Long> ids);
    //    //напиши мне метод который вернет список username на основе переданного списка id
}
