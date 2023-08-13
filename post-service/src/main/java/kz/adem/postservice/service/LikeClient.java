package kz.adem.postservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "LIKE-SERVICE")
public interface LikeClient {
    @PostMapping("/api/v1/like")
    void likePost(@RequestParam(value = "userId") Long userId,@RequestParam(value = "postId") Long postId);

    @PostMapping("/api/v1/like/unlike")
    void unlikePost(@RequestParam(value = "userId") Long userId,@RequestParam(value = "postId") Long postId);

    @GetMapping("/api/v1/like/likers")
    List<String> getPostLikers(@RequestParam Long postId);
}
