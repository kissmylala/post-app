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
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserDto> getAllEnabledUsers(){
        return userService.getAllByEnabledIsTrue();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/banned")
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserDto> getAllBannedUsers(){
       return userService.getAllByEnabledIsFalse();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/ban/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> banUser(@PathVariable(value = "username") String username){
        return userService.banUser(username);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> createUser(@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/createAdmin")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> createAdmin(@RequestBody UserDto userDto){
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return  userService.createUser(userDto);
    }


}
