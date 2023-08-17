package kz.adem.commentservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "LIKE-SERVICE")
public interface LikeClient {

    @PutMapping("/api/v1/like/comment")
    void likeComment(@RequestParam(value = "userId") Long userId, @RequestParam(value = "commentId") Long commentId);

    @PutMapping("/api/v1/like/comment/unlike")
    void unlikeComment(@RequestParam(value = "userId") Long userId, @RequestParam(value = "commentId") Long commentId);

    @GetMapping("/api/v1/like/comment/likers")
    List<String> getCommentLikers(@RequestParam Long commentId);
}