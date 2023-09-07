package kz.adem.postservice.service.impl;

import kz.adem.postservice.dto.CommentDto;
import kz.adem.postservice.dto.UserDto;
import kz.adem.postservice.feign.CommentClient;
import kz.adem.postservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentClient commentClient;
    @Override
    public String createCommentToPost(CommentDto commentDto, Long postId, String username, Long userId) {
        return commentClient.createComment(commentDto, postId, username, userId);
    }

    @Override
    public String createReplyComment(CommentDto commentDto, Long postId, String username, Long userId, Long parentCommentId) {
        return commentClient.createReplyComment(commentDto, postId, username, userId, parentCommentId);
    }

    @Override
    public String deleteComment(Long postId, Long commentId, String username, Long userId) {
        return commentClient.deleteComment(postId, commentId, username, userId);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto, String username, Long userId) {
        return commentClient.updateComment(postId, commentId, commentDto, username, userId);
    }

    @Override
    public CommentDto likeComment(Long commentId, Long userId) {
        return  commentClient.likeComment(commentId, userId);
    }

    @Override
    public CommentDto unlikeComment(Long commentId, Long userId) {
        return  commentClient.unlikeComment(commentId, userId);
    }

    @Override
    public List<UserDto> getCommentLikers(Long commentId) {
        return commentClient.getCommentLikers(commentId);
    }
}
