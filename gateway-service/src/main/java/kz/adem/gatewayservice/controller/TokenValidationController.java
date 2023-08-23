package kz.adem.gatewayservice.controller;

import kz.adem.gatewayservice.service.TokenValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenValidationController {
    private final TokenValidationService tokenValidationService;
    @GetMapping("/validate")
    public Mono<ResponseEntity<Boolean>> isTokenValid(@RequestParam(value = "token") String token){
        return tokenValidationService.isTokenValid(token)
                .map(ResponseEntity::ok);
    }
}
