package kz.adem.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.userservice.dto.UserDto;
import kz.adem.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{usernameOrEmail}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable(value = "usernameOrEmail") String usernameOrEmail){
        UserDto userDto = userService.getUserByUsernameOrEmail(usernameOrEmail);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") Long userId){
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable(value = "username") String username){
        UserDto userDto = userService.getUserByUsername(username);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "id") Long userId, @RequestBody UserDto userDto,
                                              HttpServletRequest request){
        String username = request.getHeader("user");
        if (username.equals(userDto.getUsername())){
            UserDto updatedUser = userService.updateUser(userId,userDto);
            return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        }
        throw new AccessDeniedException("Access denied");
    }
    @PostMapping("/liked/usernames")
    public ResponseEntity<List<String>> getAllUsernamesByIdIn(@RequestBody List<Long> ids){
        List<String> usernames = userService.getAllUsernamesByIdIn(ids);
        return new ResponseEntity<>(usernames,HttpStatus.OK);
    }





}
