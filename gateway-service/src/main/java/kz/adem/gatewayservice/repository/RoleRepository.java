package kz.adem.gatewayservice.repository;

import kz.adem.gatewayservice.entity.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface RoleRepository extends R2dbcRepository<Role,Long> {
    Mono<Role> findByName(String name);

}
