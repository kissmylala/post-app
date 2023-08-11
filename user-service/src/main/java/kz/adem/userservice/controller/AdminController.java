package kz.adem.userservice.controller;

import kz.adem.userservice.dto.UserDto;
import kz.adem.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<UserDto>> getAllEnabledUsers(){
        List<UserDto> allEnabledUsers = userService.getAllByEnabledIsTrue();
        return new ResponseEntity<>(allEnabledUsers, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/banned")
    public ResponseEntity<List<UserDto>> getAllBannedUsers(){
        List<UserDto> allUsers = userService.getAllByEnabledIsFalse();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/ban/{username}")
    public ResponseEntity<String> banUser(@PathVariable(value = "username") String username){
        String message = userService.banUser(username);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        UserDto newUser = userService.createUser(userDto);
        return new ResponseEntity<>(newUser,HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/createAdmin")
    public ResponseEntity<UserDto> createAdmin(@RequestBody UserDto userDto,@RequestParam(required = false) String secret){

        if (!StringUtils.hasText(secret) || !secret.equals(ADMIN_SECRET)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserDto newUser = userService.createAdmin(userDto);
        return new ResponseEntity<>(newUser,HttpStatus.CREATED);
    }


}
