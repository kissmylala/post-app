package kz.adem.userservice.service.impl;

import kz.adem.userservice.dto.UserDto;
import kz.adem.userservice.entity.Role;
import kz.adem.userservice.entity.Token;
import kz.adem.userservice.entity.User;
import kz.adem.userservice.exception.ResourceNotFoundException;
import kz.adem.userservice.mapper.UserMapper;
import kz.adem.userservice.repository.RoleRepository;
import kz.adem.userservice.repository.TokenRepository;
import kz.adem.userservice.repository.UserRepository;
import kz.adem.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(UserMapper.MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User","email",email));
        return UserMapper.MAPPER.mapToDto(user);
    }

    @Override
    public UserDto getUserByUsernameOrEmail(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow(()->new ResourceNotFoundException("User","email or username",usernameOrEmail));
        return UserMapper.MAPPER.mapToDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User","username",username));
        return UserMapper.MAPPER.mapToDto(user);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserDto> getAllByEnabledIsTrue() {
        return userRepository.findAllByEnabledIsTrue()
                .stream().map(UserMapper.MAPPER::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public UserDto createUser(UserDto userDto){
        // TODO: 03.08.23 UserAlreadyExists exception

        return createUserWithRole(userDto, ROLE_USER);
    }
    @Override
    public UserDto createAdmin(UserDto userDto){
        // TODO: 03.08.23 UserAlreadyExists exception
        return createUserWithRole(userDto, ROLE_ADMIN);
    }
    private UserDto createUserWithRole(UserDto userDto, String roleName){
        User newUser = UserMapper.MAPPER.mapToEntity(userDto);
        newUser.setCreatedAt(new Date());
        newUser.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
        roles.add(role);
        newUser.setRoles(roles);
        return UserMapper.MAPPER.mapToDto(userRepository.save(newUser));
    }

    @Override
    public List<UserDto> getAllByEnabledIsFalse() {
        return userRepository.findAllByEnabledIsFalse()
                .stream().map(UserMapper.MAPPER::mapToDto)
                .collect(Collectors.toList());    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User","id",String.valueOf(id)));
        return UserMapper.MAPPER.mapToDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        // TODO: 03.08.23 get authentication of current user
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",String.valueOf(userId)));
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User updatedUser = userRepository.save(user);
        return UserMapper.MAPPER.mapToDto(updatedUser);
    }

    @Override
    public String banUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User","username",username));
        user.setEnabled(false);
        revokeAllUsersToken(user);
        userRepository.save(user);
        return "User "+username+" is banned";
    }
    private void revokeAllUsersToken(User user){
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
