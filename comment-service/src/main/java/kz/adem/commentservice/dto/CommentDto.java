package kz.adem.commentservice.dto;

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
    private String repliedTo;
    private Long postId;
    private Long parentCommentId;
    private List<CommentDto> replies;
}

