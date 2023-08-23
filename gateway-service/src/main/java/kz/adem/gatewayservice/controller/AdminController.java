package kz.adem.gatewayservice.controller;

import kz.adem.gatewayservice.dto.UserDto;
import kz.adem.gatewayservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.account.secret}")
    private String ADMIN_SECRET;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/enabled")
    public Flux<ResponseEntity<UserDto>> getAllEnabledUsers(){
        return userService.getAllByEnabledIsTrue()
                .map(ResponseEntity::ok);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/banned")
    public Flux<ResponseEntity<UserDto>> getAllBannedUsers(){
       return userService.getAllByEnabledIsFalse()
               .map(ResponseEntity::ok);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/ban/{username}")
    public Mono<ResponseEntity<String>> banUser(@PathVariable(value = "username") String username){
        return userService.banUser(username)
                .map(ResponseEntity::ok);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public Mono<ResponseEntity<UserDto>> createUser(@RequestBody UserDto userDto){
        return userService.createUser(userDto)
                .map(ResponseEntity::ok);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/createAdmin")
    public Mono<ResponseEntity<UserDto>> createAdmin(@RequestBody UserDto userDto,@RequestParam(required = false) String secret){

        if (!StringUtils.hasText(secret) || !secret.equals(ADMIN_SECRET)){
            return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return  userService.createUser(userDto)
                .map(ResponseEntity::ok);
    }


}
