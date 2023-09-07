package kz.adem.commentservice.controller;

import kz.adem.commentservice.dto.CommentDto;
import kz.adem.commentservice.dto.UserDto;
import kz.adem.commentservice.service.CommentService;
import kz.adem.commentservice.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comments")
public class LikeController {
    private final LikeService likeService;
    private final CommentService commentService;
    @GetMapping("/{commentId}/likers")
    public ResponseEntity<List<UserDto>> getCommentLikers(@PathVariable("commentId") Long commentId){
        return ResponseEntity.ok(likeService.getUsersWhoLiked(commentId));
    }
    @PostMapping("/{commentId}/like")
    public ResponseEntity<CommentDto> likeComment(@PathVariable("commentId") Long commentId, @RequestParam("userId") Long userId){
        likeService.like(commentId,userId);
        CommentDto commentDto = commentService.getCommentById(commentId);
        return ResponseEntity.ok(commentDto);
    }
    @DeleteMapping("/{commentId}/unlike")
    public ResponseEntity<CommentDto> unlikeComment(@PathVariable("commentId") Long commentId,@RequestParam("userId") Long userId){
        likeService.unlike(commentId,userId);
        CommentDto commentDto = commentService.getCommentById(commentId);
        return ResponseEntity.ok(commentDto);
    }
}
