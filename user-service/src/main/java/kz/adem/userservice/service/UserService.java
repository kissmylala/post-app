package kz.adem.userservice.service;

import kz.adem.userservice.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserByEmail(String email);
    UserDto getUserById(Long id);
    UserDto getUserByUsernameOrEmail(String usernameOrEmail);
    UserDto getUserByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<UserDto> getAllByEnabledIsTrue();
    List<UserDto> getAllByEnabledIsFalse();
    UserDto updateUser(Long userId,UserDto userDto);
    UserDto createUser(UserDto userDto);
    UserDto createAdmin(UserDto userDto);

    String banUser(String username);



}
