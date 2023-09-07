package kz.adem.postservice.service.impl;

import kz.adem.postservice.dto.UserDto;
import kz.adem.postservice.entity.Like;
import kz.adem.postservice.entity.Post;
import kz.adem.postservice.exception.ResourceNotFoundException;
import kz.adem.postservice.repository.LikeRepository;
import kz.adem.postservice.repository.PostRepository;
import kz.adem.postservice.service.LikeService;
import kz.adem.postservice.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserClient userClient;
    private final PostRepository postRepository;
    @Override
    public void likePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","postId",String.valueOf(postId)));
        Like like = Like.builder()
                .userId(userId)
                .post(post)
                .build();
        likeRepository.save(like);
        post.setLikes(post.getLikes()+1);
        postRepository.save(post);
    }

    @Override
    public void unlikePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","postId",String.valueOf(postId)));
        Like like = likeRepository.findByUserIdAndPostId(userId,postId);
        likeRepository.delete(like);
        post.setLikes(post.getLikes()-1);
        postRepository.save(post);
     }

    @Override
    public List<UserDto> getUsersWhoLikedPost(Long postId) {
        List<Long> userIds = likeRepository.findAllByPostId(postId)
                .stream().map(Like::getUserId).toList();
        return userClient.getAllUsersByIdIn(userIds);
    }
}
