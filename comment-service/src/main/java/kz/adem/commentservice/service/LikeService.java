package kz.adem.commentservice.service;

import kz.adem.commentservice.dto.UserDto;

import java.util.List;

public interface LikeService {
    void like(Long userId, Long commentId);
    void unlike(Long userId, Long commentId);
    List<UserDto> getUsersWhoLiked(Long commentId);
}
