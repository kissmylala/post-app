package kz.adem.commentservice.mapper;

import kz.adem.commentservice.dto.CommentDto;
import kz.adem.commentservice.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);
    @Mapping(source = "parentComment.id", target = "parentCommentId")
    CommentDto mapToDto (Comment comment);
    @Mapping(target = "parentComment", ignore = true)
    Comment mapToEntity (CommentDto commentDto);
}
