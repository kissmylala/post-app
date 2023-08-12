package kz.adem.likeservice.service.impl;

import kz.adem.likeservice.entity.PostLike;
import kz.adem.likeservice.kafka.PostLikeEventProducer;
import kz.adem.likeservice.kafka.PostLikeMessage;
import kz.adem.likeservice.repository.PostLikeRepository;
import kz.adem.likeservice.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostLikeEventProducer postLikeEventProducer;

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
    public void dislikePost(Long userId, Long postId) {

    }

    @Override
    public boolean isLiked(Long userId, Long postId) {
        return false;
    }

    @Override
    public Long countByPostId(Long postId) {
        return null;
    }
}
