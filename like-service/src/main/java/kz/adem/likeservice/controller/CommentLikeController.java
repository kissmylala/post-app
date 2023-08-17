package kz.adem.likeservice.controller;

import kz.adem.likeservice.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/like/comment")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PutMapping
    public ResponseEntity<Void> likeComment(@RequestParam Long userId,@RequestParam Long commentId) {
        commentLikeService.like(userId, commentId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/unlike")
    public ResponseEntity<Void> unlikeComment(@RequestParam Long userId,@RequestParam Long commentId) {
        commentLikeService.unlike(userId, commentId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/likers")
    public ResponseEntity<List<String>> getCommentLikers(@RequestParam Long commentId) {
        List<String> commentLikers = commentLikeService.getLikedUsernames(commentId);
        return ResponseEntity.ok(commentLikers);
    }

}
