package kz.adem.likeservice.service.impl;

import kz.adem.likeservice.entity.CommentLike;
import kz.adem.likeservice.repository.CommentLikeRepository;
import kz.adem.likeservice.service.CommentLikeService;
import kz.adem.likeservice.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final UserClient userClient;
    @Override
    public void like(Long userId, Long commentId) {
        CommentLike like = CommentLike.builder()
                .userId(userId)
                .commentId(commentId)
                .build();
        commentLikeRepository.save(like);
    }

    @Override
    public void unlike(Long userId, Long commentId) {
        CommentLike like = commentLikeRepository.findByUserIdAndCommentId(userId,commentId);
        commentLikeRepository.delete(like);

    }

    @Override
    public boolean isLiked(Long userId, Long commentId) {
        return false;
    }

    @Override
    public List<String> getLikedUsernames(Long commentId) {
        List<Long> userIds = commentLikeRepository.findAllByCommentId(commentId)
                .stream().map(CommentLike::getUserId).toList();
        List<String> usernames = userClient.getAllUsernamesByIdIn(userIds);
        return usernames;
    }

}
