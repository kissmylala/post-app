package kz.adem.gatewayservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.adem.gatewayservice.dto.UserDto;
import kz.adem.gatewayservice.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Only admin access endpoints
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin controller", description = "Operations for admin only")
public class AdminController {
    private final UserService userService;


    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "201", description = "Successfully created user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> createUser(@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    @Operation(summary = "Ban user by username")
    @ApiResponse(responseCode = "200", description = "Successfully banned user with specified username")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/ban/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> banUser(@PathVariable(value = "username") String username){
        return userService.banUser(username);
    }

    @Operation(summary = "Create admin")
    @ApiResponse(responseCode = "201", description = "Successfully created admin account")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/createAdmin")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> createAdmin(@RequestBody UserDto userDto){
        return  userService.createUser(userDto);
    }

    @Operation(summary = "Retrieve list of enabled users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of enabled users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/enabled")
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserDto> getAllEnabledUsers(){
        return userService.getAllByEnabledIsTrue();
    }

    @Operation(summary = "Retrieve list of banned users")
    @ApiResponse(responseCode = "200", description = "Retrieved list of banned users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/banned")
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserDto> getAllBannedUsers(){
       return userService.getAllByEnabledIsFalse();
    }






}
