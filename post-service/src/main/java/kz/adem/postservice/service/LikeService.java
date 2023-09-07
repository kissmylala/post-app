package kz.adem.postservice.service;

import kz.adem.postservice.dto.UserDto;

import java.util.List;

public interface LikeService {
    void likePost(Long userId, Long postId);

    void unlikePost(Long userId, Long postId);

    List<UserDto> getUsersWhoLikedPost(Long postId);

}
