package kz.adem.postservice.dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostCommentDto {
    private PostDto postDto;
    private List<CommentDto> commentDtoList;
}
