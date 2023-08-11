package kz.adem.userservice.controller;

import kz.adem.userservice.service.TokenValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenValidationController {
    private final TokenValidationService tokenValidationService;
    @GetMapping("/validate")
    public ResponseEntity<Boolean> isTokenValid(@RequestParam(value = "token") String token){
        return ResponseEntity.ok(tokenValidationService.isTokenValid(token));
    }
}
