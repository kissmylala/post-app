package kz.adem.postservice.mapper;

import kz.adem.postservice.dto.PostDto;
import kz.adem.postservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper MAPPER = Mappers.getMapper(PostMapper.class);
    PostDto mapToDto(Post post);
    Post mapToEntity(PostDto postDto);
}
