package kz.adem.likeservice.controller;

import kz.adem.likeservice.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;
    @PostMapping
    ResponseEntity<Void> likePost(@RequestParam Long userId,@RequestParam Long postId){
        postLikeService.likePost(userId,postId);
        return ResponseEntity.ok().build();
    }
}
