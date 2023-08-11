package kz.adem.userservice.mapper;

import kz.adem.userservice.dto.UserDto;
import kz.adem.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    UserDto mapToDto(User user);
    User mapToEntity(UserDto userDto);

}