package kz.adem.likeservice.controller;

import kz.adem.likeservice.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;
    @PostMapping
    public ResponseEntity<Void> likePost(@RequestParam Long userId,@RequestParam Long postId){
        postLikeService.likePost(userId,postId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/unlike")
    public ResponseEntity<Void> unlikePost(@RequestParam Long userId,@RequestParam Long postId){
        postLikeService.unlikePost(userId,postId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/likers")
    public ResponseEntity<List<String>> getPostLikers(@RequestParam Long postId){
        List<String> postLikers = postLikeService.getLikedUsernames(postId);
        return ResponseEntity.ok(postLikers);
    }
}
