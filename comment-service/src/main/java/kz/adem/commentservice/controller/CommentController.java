package kz.adem.commentservice.controller;

import kz.adem.commentservice.dto.CommentDto;
import kz.adem.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CommentDto commentDto,@RequestParam("postId") Long postId,
                                                @RequestParam("username") String username,@RequestParam("userId") Long userId) {
        CommentDto userComment = commentService.createCommentWithUser(username,userId);
        commentDto.setUsername(userComment.getUsername());
        commentDto.setUserId(userComment.getUserId());
        commentService.createComment(postId, commentDto);
        return ResponseEntity.ok("Comment created");
    }
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));

    }
    @PostMapping("/{parentCommentId}")
    public ResponseEntity<String> createReplyComment(@RequestBody CommentDto commentDto, @RequestParam("postId") Long postId,
                                                     @RequestParam("username") String username, @RequestParam("userId") Long userId,
                                                     @PathVariable("parentCommentId") Long parentCommentId) {
        CommentDto userComment = commentService.createCommentWithUser(username,userId);
        commentDto.setUsername(userComment.getUsername());
        commentDto.setUserId(userComment.getUserId());
        commentService.createReplyComment(postId, commentDto, parentCommentId);
        return ResponseEntity.ok("Child comment created");
    }
    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                @RequestParam("username") String username, @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(commentService.deleteComment(postId, commentId, username, userId));
    }
}

