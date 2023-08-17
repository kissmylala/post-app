package kz.adem.likeservice.service;

import java.util.List;

public interface PostLikeService {
    void likePost(Long userId, Long postId);
    void unlikePost(Long userId, Long postId);

    List<String> getLikedUsernames(Long postId);
}
