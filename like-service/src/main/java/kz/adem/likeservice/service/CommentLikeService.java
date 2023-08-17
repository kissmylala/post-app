package kz.adem.likeservice.service;

import java.util.List;

public interface CommentLikeService {
    void like(Long userId, Long commentId);
    void unlike(Long userId, Long commentId);
    boolean isLiked(Long userId, Long commentId);
    List<String> getLikedUsernames(Long commentId);

}
