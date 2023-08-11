package kz.adem.likeservice.service;

public interface PostLikeService {
    void likePost(Long userId, Long postId);
    void dislikePost(Long userId, Long postId);
    boolean isLiked(Long userId, Long postId);
    Long countByPostId(Long postId);
}
