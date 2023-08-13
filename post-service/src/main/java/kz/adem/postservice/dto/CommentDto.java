package kz.adem.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String username;
    private Long userId;
    private String body;
    private Long postId;
    private Long parentCommentId;
    private List<CommentDto> replies;
}