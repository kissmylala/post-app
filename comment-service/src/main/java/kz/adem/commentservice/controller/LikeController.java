package kz.adem.commentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Like Controller", description = "Operations related to liking and unliking comments")
public class LikeController {
    private final LikeService likeService;
    private final CommentService commentService;

    @Operation(summary = "Retrieve users who liked a specific comment")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users who liked the comment")
    @GetMapping("/{commentId}/likers")
    public ResponseEntity<List<UserDto>> getCommentLikers(@PathVariable("commentId") Long commentId){
        return ResponseEntity.ok(likeService.getUsersWhoLiked(commentId));
    }

    @Operation(summary = "Like a specific comment")
    @ApiResponse(responseCode = "200", description = "Successfully liked the comment and returned the updated comment details")
    @PostMapping("/{commentId}/like")
    public ResponseEntity<CommentDto> likeComment(@PathVariable("commentId") Long commentId, @RequestParam("userId") Long userId){
        likeService.like(commentId,userId);
        CommentDto commentDto = commentService.getCommentById(commentId);
        return ResponseEntity.ok(commentDto);
    }
    @Operation(summary = "Unlike a specific comment")
    @ApiResponse(responseCode = "200", description = "Successfully unliked the comment and returned the updated comment details")
    @DeleteMapping("/{commentId}/unlike")
    public ResponseEntity<CommentDto> unlikeComment(@PathVariable("commentId") Long commentId,@RequestParam("userId") Long userId){
        likeService.unlike(commentId,userId);
        CommentDto commentDto = commentService.getCommentById(commentId);
        return ResponseEntity.ok(commentDto);
    }
}
