package kz.adem.gatewayservice.service.impl;

import kz.adem.gatewayservice.dto.UserDto;
import kz.adem.gatewayservice.entity.Role;
import kz.adem.gatewayservice.entity.User;
import kz.adem.gatewayservice.exception.ResourceNotFoundException;
import kz.adem.gatewayservice.mapper.UserMapper;
import kz.adem.gatewayservice.repository.RoleRepository;
import kz.adem.gatewayservice.repository.TokenRepository;
import kz.adem.gatewayservice.repository.UserRepository;
import kz.adem.gatewayservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Flux<UserDto> getAllUsers() {
        return userRepository.findAll()
                .map(UserMapper.MAPPER::mapToDto);
    }

    @Override
    public Mono<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper.MAPPER::mapToDto)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User","email",email)));
    }



    @Override
    public Mono<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper.MAPPER::mapToDto)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User","username",username)));
    }

    @Override
    public Mono<Boolean> existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Flux<UserDto> getAllByEnabledIsTrue() {
        return userRepository.findAllByEnabledIsTrue()
                .map(UserMapper.MAPPER::mapToDto);

    }
    @Override
    public Mono<UserDto> createUser(UserDto userDto){
        // TODO: 03.08.23 UserAlreadyExists exception
        return createUserWithRole(userDto, ROLE_USER);
    }
    @Override
    public Mono<UserDto> createAdmin(UserDto userDto){
        // TODO: 03.08.23 UserAlreadyExists exception
        return createUserWithRole(userDto, ROLE_ADMIN);
    }
    private Mono<UserDto> createUserWithRole(UserDto userDto, String roleName){
        User newUser = UserMapper.MAPPER.mapToEntity(userDto);
        newUser.setCreatedAt(new Timestamp(new Date().getTime()));
        newUser.setEnabled(true);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return roleRepository.findByName(roleName)
                .doOnNext(role -> {
                    Set<Role> roles = new HashSet<>();
                    roles.add(role);
                    newUser.setRoleId(role.getId());
                })
                .flatMap(role -> userRepository.save(newUser))
                .map(UserMapper.MAPPER::mapToDto);
    }

    @Override
    public Flux<UserDto> getAllByEnabledIsFalse() {
        return userRepository.findAllByEnabledIsFalse()
                .map(UserMapper.MAPPER::mapToDto);
                   }

    @Override
    public Mono<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper.MAPPER::mapToDto)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User","id",String.valueOf(id))));
    }

    @Override
    public Mono<UserDto> updateUser(Long userId, UserDto userDto) {
       return userRepository.findById(userId)
               .doOnNext(user->{
                     user.setEmail(userDto.getEmail());
                     user.setName(userDto.getName());
                     user.setUsername(userDto.getUsername());
                     user.setPassword(passwordEncoder.encode(userDto.getPassword()));

                })
               .flatMap(userRepository::save)
               .map(UserMapper.MAPPER::mapToDto);

               }


    @Override
    public Mono<String> banUser(String username) {
        return userRepository.findByUsername(username)
                .doOnNext(user->{
                    user.setEnabled(false);
                    revokeAllUsersToken(user);
                })
                .flatMap(userRepository::save)
                .map(user->"User "+username+" is banned")
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User","username",username)));
    }

    private void revokeAllUsersToken(User user){
         tokenRepository.findAllValidTokenByUser(user.getId())
                .doOnNext(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                })
                .flatMap(tokenRepository::save)
                .subscribe();
    }

    @Override
    public Flux<String> getAllUsernamesByIdIn(List<Long> ids) {
        return userRepository.findAllByIdIn(ids)
                .map(User::getUsername);
    }

    @Override
    public Flux<UserDto> getAllUsersByIdIn(List<Long> ids) {
        return userRepository.findAllByIdIn(ids)
                .map(UserMapper.MAPPER::mapToDto);
    }
}
