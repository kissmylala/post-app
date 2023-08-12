package kz.adem.postservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "LIKE-SERVICE")
public interface LikeClient {
    @PostMapping("/api/v1/like")
    void likePost(@RequestParam(value = "userId") Long userId,@RequestParam(value = "postId") Long postId);

}
