package kz.adem.gatewayservice.mapper;

import kz.adem.gatewayservice.dto.UserDto;
import kz.adem.gatewayservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserDto mapToDto(User user);

    User mapToEntity(UserDto userDto);

}