package kz.adem.postservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long repliedTo;
    private Long likes;
    @JsonIgnore
    private Long postId;
    @JsonIgnore
    private Long parentCommentId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentDto> replies;
}