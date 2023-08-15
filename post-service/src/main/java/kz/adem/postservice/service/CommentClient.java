package kz.adem.postservice.service;

import jakarta.ws.rs.Path;
import kz.adem.postservice.dto.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "COMMENT-SERVICE")
public interface CommentClient {
    @PostMapping("/api/v1/comments")
        String createComment (@RequestBody CommentDto commentDto, @RequestParam("postId") Long postId,
                @RequestParam("username") String username, @RequestParam("userId") Long userId);
    @GetMapping("/api/v1/comments/{postId}")
    List<CommentDto> getCommentsByPostId(@PathVariable Long postId);

    @PostMapping("/api/v1/comments/{parentCommentId}")
    String createReplyComment(@RequestBody CommentDto commentDto,@RequestParam("postId") Long postId,
                            @RequestParam("username") String username,@RequestParam("userId") Long userId,
                              @PathVariable Long parentCommentId);
    @DeleteMapping("/api/v1/comments/{postId}/{commentId}")
    String deleteComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId,
                         @RequestParam("username") String username, @RequestParam("userId") Long userId);

    @PutMapping("/api/v1/comments/{postId}/{commentId}")
    CommentDto updateComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId,
                             @RequestBody CommentDto commentDto, @RequestParam("username") String username,
                             @RequestParam("userId") Long userId);
}
