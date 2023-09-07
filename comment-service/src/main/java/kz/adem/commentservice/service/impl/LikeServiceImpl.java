package kz.adem.commentservice.service.impl;

import kz.adem.commentservice.dto.UserDto;
import kz.adem.commentservice.entity.Comment;
import kz.adem.commentservice.entity.Like;
import kz.adem.commentservice.exception.ResourceNotFoundException;
import kz.adem.commentservice.repository.CommentRepository;
import kz.adem.commentservice.repository.LikeRepository;
import kz.adem.commentservice.service.LikeService;
import kz.adem.commentservice.feign.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserClient userClient;
    @Override
    public void like(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",String.valueOf(commentId)));
        Like like = Like.builder()
                .userId(userId)
                .comment(comment)
                .build();
        comment.setLikes(comment.getLikes()+1);
        likeRepository.save(like);
        commentRepository.save(comment);
    }

    @Override
    public void unlike(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",String.valueOf(commentId)));
        Like like = likeRepository.findByUserIdAndCommentId(userId,commentId);
        likeRepository.delete(like);
        comment.setLikes(comment.getLikes()-1);
        commentRepository.save(comment);
    }

    @Override
    public List<UserDto> getUsersWhoLiked(Long commentId) {
        List<Long> userIds = likeRepository.findAllByCommentId(commentId)
                .stream().map(Like::getUserId).toList();
        return userClient.getAllUsersByIdIn(userIds);
    }
}
