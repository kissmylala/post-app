package kz.adem.commentservice.controller;

import jakarta.ws.rs.Path;
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
    @PutMapping("/{postId}/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId,
                                                    @RequestBody CommentDto commentDto, @RequestParam("username") String username,
                                                    @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(commentService.updateComment(postId, commentId, commentDto, username, userId));
    }
    @GetMapping("/commentId/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("commentId") Long commentId){
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }


    }





