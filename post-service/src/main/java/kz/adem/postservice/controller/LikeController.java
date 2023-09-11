package kz.adem.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kz.adem.postservice.dto.CommentDto;
import kz.adem.postservice.dto.PostDto;
import kz.adem.postservice.dto.UserDto;
import kz.adem.postservice.service.CommentService;
import kz.adem.postservice.service.LikeService;
import kz.adem.postservice.service.PostService;
import kz.adem.postservice.util.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kz.adem.postservice.util.ControllerUtils.getUserIdFromRequest;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
@Tag(name = "Like controller", description = "Operations related to post likes")
public class LikeController {
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;

    @Operation(summary = "Like a post")
    @ApiResponse(responseCode = "200",description = "Successfully liked the post")
    @PostMapping("/{postId}/like")
    public ResponseEntity<PostDto> likePost(@PathVariable(value = "postId") Long postId, HttpServletRequest request){
        Long userId = ControllerUtils.getUserIdFromRequest(request);
        likeService.likePost(userId,postId);
        PostDto likedPost = postService.getPostById(postId);
        return new ResponseEntity<>(likedPost, HttpStatus.OK);
    }
    @Operation(summary = "Like a comment")
    @ApiResponse(responseCode = "200", description = "Successfully liked the comment")
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<CommentDto> likeComment(
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest request){
        Long userId = getUserIdFromRequest(request);
        CommentDto likedComment = commentService.likeComment(commentId,userId);
        return new ResponseEntity<>(likedComment,HttpStatus.OK);
    }

    @Operation(summary = "Get users who liked a post")
    @ApiResponse(responseCode = "200",description = "Successfully retrieved list of users who liked the post")
    @GetMapping("/{postId}/likers")
    public ResponseEntity<List<UserDto>> getPostLikers(@PathVariable(value = "postId") Long postId){
        List<UserDto> postLikers = likeService.getUsersWhoLikedPost(postId);
        return new ResponseEntity<>(postLikers,HttpStatus.OK);
    }
    @Operation(summary = "Get users who liked a comment")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users who liked the comment")
    @GetMapping("/comments/{commentId}/likers")
    public ResponseEntity<List<UserDto>> getCommentLikers(@PathVariable("commentId") Long commentId){
        List<UserDto> commentLikers = commentService.getCommentLikers(commentId);
        return new ResponseEntity<>(commentLikers,HttpStatus.OK);
    }

    @Operation(summary = "Unlike a post")
    @ApiResponse(responseCode = "200",description = "Successfully unliked the post")
    @DeleteMapping("/{postId}/unlike")
    public ResponseEntity<PostDto> unlikePost(@PathVariable(value = "postId") Long postId,HttpServletRequest request){
        Long userId = ControllerUtils.getUserIdFromRequest(request);
        likeService.unlikePost(userId,postId);
        PostDto likedPost = postService.getPostById(postId);
        return new ResponseEntity<>(likedPost,HttpStatus.OK);
    }
    @Operation(summary = "Unlike a comment")
    @ApiResponse(responseCode = "200", description = "Successfully unliked the comment")
    @DeleteMapping("/comments/{commentId}/unlike")
    public ResponseEntity<CommentDto> unlikeComment(
            @PathVariable(value = "commentId") Long commentId,
            HttpServletRequest request){
        Long userId = getUserIdFromRequest(request);
        CommentDto likedComment = commentService.unlikeComment(commentId,userId);
        return new ResponseEntity<>(likedComment,HttpStatus.OK);
    }


}
