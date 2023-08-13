package kz.adem.postservice.service;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "COMMENT-SERVICE")
public class CommentClient {
    @PostMapping("api/v1/comments/{postId}")
    String createComment()
}
