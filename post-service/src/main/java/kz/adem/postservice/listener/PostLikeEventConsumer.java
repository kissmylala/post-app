package kz.adem.postservice.listener;

import kz.adem.likeservice.kafka.PostLikeMessage;
import kz.adem.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeEventConsumer {
    private final PostService postService;
    @KafkaListener(topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(PostLikeMessage message){
        log.info("Received like event: {}", message);
        Long userId = message.getUserId();
        Long postId = message.getPostId();
        postService.incrementLikes(postId);
    }
}
