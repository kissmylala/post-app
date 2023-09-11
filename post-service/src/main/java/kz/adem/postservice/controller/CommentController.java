package kz.adem.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kz.adem.postservice.dto.CommentDto;
import kz.adem.postservice.dto.PostCommentDto;
import kz.adem.postservice.dto.UserDto;
import kz.adem.postservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kz.adem.postservice.util.ControllerUtils.getUserIdFromRequest;
import static kz.adem.postservice.util.ControllerUtils.getUsernameFromRequest;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "Operations related to post comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Create a comment for a post")
    @ApiResponse(responseCode = "201", description = "Successfully created the comment for the post")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<PostCommentDto> createCommentToPost(@RequestBody CommentDto commentDto,
                                                      @PathVariable(value = "postId") Long postId,
                                                      HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        PostCommentDto postCommentDto = commentService.createCommentToPost(commentDto,postId,username,userId);
        return new ResponseEntity<>(postCommentDto, HttpStatus.CREATED);
    }
    @Operation(summary = "Create a reply comment for a comment")
    @ApiResponse(responseCode = "201", description = "Successfully created the reply comment")
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





    @Operation(summary = "Update a comment in a post")
    @ApiResponse(responseCode = "200", description = "Successfully updated the comment in the post")
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

    @Operation(summary = "Delete a comment from a post")
    @ApiResponse(responseCode = "200", description = "Successfully deleted the comment from the post")
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<PostCommentDto> deleteComment(@PathVariable(value = "postId") Long postId,
                                                        @PathVariable(value = "commentId") Long commentId,
                                                        HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        PostCommentDto postCommentDto = commentService.deleteComment(postId,commentId,username,userId);
        return new ResponseEntity<>(postCommentDto,HttpStatus.OK);
    }




}
