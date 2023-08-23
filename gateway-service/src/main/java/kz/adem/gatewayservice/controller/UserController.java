package kz.adem.gatewayservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.gatewayservice.dto.UserDto;
import kz.adem.gatewayservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public Flux<ResponseEntity<UserDto>> getAllUsers(){
        return userService.getAllUsers()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{email}")
    public Mono<ResponseEntity<UserDto>> getUserByEmail(@PathVariable(value = "email") String email){
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable(value = "id") Long userId){
        return userService.getUserById(userId)
                .map(ResponseEntity::ok);
    }
    @GetMapping("/username/{username}")
    public Mono<ResponseEntity<UserDto>> getUserByUsername(@PathVariable(value = "username") String username){
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable(value = "id") Long userId, @RequestBody UserDto userDto,
                                              ServerWebExchange webExchange){
        String username = webExchange.getRequest().getHeaders().getFirst("user");
        if (username.equals(userDto.getUsername())){
            return userService.updateUser(userId,userDto)
                    .map(ResponseEntity::ok);
        }
        return Mono.error(new AccessDeniedException("Access denied"));
    }
    @PostMapping("/liked/usernames")
    public Flux<ResponseEntity<String>> getAllUsernamesByIdIn(@RequestBody List<Long> ids){
        return userService.getAllUsernamesByIdIn(ids)
                .map(ResponseEntity::ok);
    }
}
