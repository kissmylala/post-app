package kz.adem.userservice.service;

public interface TokenValidationService {
    boolean isTokenValid(String token);
}
