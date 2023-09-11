package kz.adem.commentservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotEmpty
    @Size(min = 1, message = "Comment should have at least 1 character.")
    private String body;
    private Long repliedTo;
    private Long likes;
    private Long postId;
    private Long parentCommentId;
    private List<CommentDto> replies;
}

