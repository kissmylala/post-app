package kz.adem.postservice.service;

import kz.adem.postservice.dto.CommentDto;
import kz.adem.postservice.dto.UserDto;

import java.util.List;

public interface CommentService {
    String createReplyComment(CommentDto commentDto, Long postId, String username, Long userId, Long parentCommentId);
    String deleteComment(Long postId, Long commentId, String username, Long userId);
    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto, String username, Long userId);
    CommentDto likeComment(Long commentId, Long userId);
    CommentDto unlikeComment(Long commentId, Long userId);
    List<UserDto> getCommentLikers(Long commentId);

    // TODO: 07.09.23 refactor to return post with comments dto
    String createCommentToPost(CommentDto commentDto, Long postId,String username, Long userId);

}
