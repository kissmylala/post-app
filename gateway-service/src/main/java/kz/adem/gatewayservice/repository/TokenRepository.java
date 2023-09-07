package kz.adem.gatewayservice.repository;

import kz.adem.gatewayservice.entity.Token;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TokenRepository extends R2dbcRepository<Token, Integer> {

    @Query(value = """
      SELECT t.* FROM user_tokens t 
      INNER JOIN users u ON t.user_id = u.id
      WHERE u.id = :id AND (t.expired = FALSE OR t.revoked = FALSE)
      """)
    Flux<Token> findAllValidTokenByUser(Long id);

    Mono<Token> findByToken(String token);
}