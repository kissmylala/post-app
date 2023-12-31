package kz.adem.commentservice.service;

import kz.adem.commentservice.dto.CommentDto;

import java.util.List;

public interface CommentService {
    //напиши мне метод для создания комментария
    CommentDto createComment(Long postId, CommentDto commentDto);
    CommentDto createCommentWithUser(String username, Long userId);
    List<CommentDto> getCommentsByPostId(Long postId);
    void createReplyComment(Long postId, CommentDto commentDto, Long parentCommentId);
    CommentDto deleteComment(Long postId, Long commentId, String username, Long userId);
    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto,String username, Long userId);
    CommentDto getCommentById(Long commentId);
//    CommentDto likeComment(Long userId, Long commentId);
//    CommentDto unlikeComment(Long userId, Long commentId);
//    List<String> getCommentLikers(Long commentId);

}
