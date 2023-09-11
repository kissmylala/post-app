package kz.adem.gatewayservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User controller", description = "Operations related to users")
public class UserController {
    private final UserService userService;


    @Operation(summary = "Get all users by list of IDs")
    @ApiResponse(responseCode = "200",description = "Successfully retrieved list of users by list of IDs")
    @PostMapping("/liked/users")
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserDto> getAllUsersByIdIn(@RequestBody List<Long> ids){
        return userService.getAllUsersByIdIn(ids);
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieve list of all users")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by email")
    @ApiResponse(responseCode = "200", description = "Successfully got user by email")
    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserDto> getUserByEmail(@PathVariable(value = "email") String email){
        return userService.getUserByEmail(email);

    }
    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "Successfully got user by ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserDto> getUserById(@PathVariable(value = "id") Long userId){
        return userService.getUserById(userId);
    }
    @Operation(summary = "Get user by username")
    @ApiResponse(responseCode = "200", description = "Successfully got user by username")
    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserDto> getUserByUsername(@PathVariable(value = "username") String username){
        return userService.getUserByUsername(username);
    }
    @Operation(summary = "Update user data by ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated user by ID")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserDto> updateUser(@PathVariable(value = "id") Long userId, @RequestBody UserDto userDto,
                                              ServerWebExchange webExchange){
        String username = webExchange.getRequest().getHeaders().getFirst("user");
        if (username.equals(userDto.getUsername())){
            return userService.updateUser(userId,userDto);
        }
        return Mono.error(new AccessDeniedException("Access denied"));
    }


}
