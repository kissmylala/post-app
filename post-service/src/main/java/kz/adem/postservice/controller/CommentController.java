package kz.adem.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.postservice.dto.CommentDto;
import kz.adem.postservice.dto.UserDto;
import kz.adem.postservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kz.adem.util.ControllerUtils.getUserIdFromRequest;
import static kz.adem.util.ControllerUtils.getUsernameFromRequest;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // TODO: 07.09.23 should return PostDto with comments 
    @PostMapping("/{postId}/comments")
    public ResponseEntity<String> createCommentToPost(@RequestBody CommentDto commentDto,
                                                      @PathVariable(value = "postId") Long postId,
                                                      HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        String response = commentService.createCommentToPost(commentDto,postId,username,userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/{postId}/comments/{parentCommentId}")
    public ResponseEntity<String> createChildCommentToComment(@RequestBody CommentDto commentDto,
                                                              @PathVariable(value = "postId") Long postId,
                                                              HttpServletRequest request,
                                                              @PathVariable(value = "parentCommentId") Long parentCommentId){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        String response = commentService.createReplyComment(commentDto,postId,username,userId,parentCommentId);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "commentId") Long commentId,
                                                HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        String response = commentService.deleteComment(postId,commentId,username,userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postId") Long postId,
                                                    @PathVariable("commentId") Long commentId,
                                                    @RequestBody CommentDto commentDto,
                                                    HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        CommentDto updatedComment = commentService.updateComment(postId,commentId,commentDto,username,userId);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    // TODO: 07.09.23 delete {postId}
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<CommentDto> likeComment(
                                                  @PathVariable(value = "commentId") Long commentId,
                                                  HttpServletRequest request){
        Long userId = getUserIdFromRequest(request);
        CommentDto likedComment = commentService.likeComment(commentId,userId);
        return new ResponseEntity<>(likedComment,HttpStatus.OK);
    }

    // TODO: 07.09.23 delete {postId}
    @DeleteMapping("/comments/{commentId}/unlike")
    public ResponseEntity<CommentDto> unlikeComment(
                                                    @PathVariable(value = "commentId") Long commentId,
                                                    HttpServletRequest request){
        Long userId = getUserIdFromRequest(request);
        CommentDto likedComment = commentService.unlikeComment(commentId,userId);
        return new ResponseEntity<>(likedComment,HttpStatus.OK);
    }
    @GetMapping("/comments/{commentId}/likers")
    public ResponseEntity<List<UserDto>> getCommentLikers(@PathVariable("commentId") Long commentId){
        List<UserDto> commentLikers = commentService.getCommentLikers(commentId);
        return new ResponseEntity<>(commentLikers,HttpStatus.OK);
    }
}
