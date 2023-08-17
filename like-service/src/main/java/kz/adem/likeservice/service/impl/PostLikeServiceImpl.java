package kz.adem.likeservice.service.impl;

import kz.adem.likeservice.entity.PostLike;
import kz.adem.likeservice.kafka.PostLikeEventProducer;
import kz.adem.likeservice.kafka.PostLikeMessage;
import kz.adem.likeservice.repository.PostLikeRepository;
import kz.adem.likeservice.service.PostLikeService;
import kz.adem.likeservice.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostLikeEventProducer postLikeEventProducer;
    private final UserClient userClient;

    @Override
    public void likePost(Long userId, Long postId) {
        PostLike postLike = PostLike
                .builder()
                .userId(userId)
                .postId(postId)
                .build();
        postLikeRepository.save(postLike);
        PostLikeMessage postLikeMessage = PostLikeMessage
                .builder()
                .postId(postId)
                .userId(userId)
                .date(new Date())
                .build();
        postLikeEventProducer.sendLikeEvent(postLikeMessage);
    }

    @Override
    public void unlikePost(Long userId, Long postId) {
      PostLike postLike = postLikeRepository.findByUserIdAndPostId(userId,postId);
        postLikeRepository.delete(postLike);
    }

    @Override
    public List<String> getLikedUsernames(Long postId) {
        List<Long> userIds = postLikeRepository.findAllByPostId(postId)
                .stream().map(PostLike::getUserId).toList();
        List<String> usernames = userClient.getAllUsernamesByIdIn(userIds);
        return usernames;
    }


}
