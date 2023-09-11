package kz.adem.commentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.adem.commentservice.dto.CommentDto;
import kz.adem.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "Operations related to post comments")
public class CommentController {
    private final CommentService commentService;
    @Operation(summary = "Create a comment for a post")
    @ApiResponse(responseCode = "201", description = "Successfully created the comment for the post")
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody @Validated CommentDto commentDto,@RequestParam("postId") Long postId,
                                                @RequestParam("username") String username,@RequestParam("userId") Long userId) {
        CommentDto userComment = commentService.createCommentWithUser(username,userId);
        commentDto.setUsername(userComment.getUsername());
        commentDto.setUserId(userComment.getUserId());
        return ResponseEntity.ok(commentService.createComment(postId, commentDto));
    }

    @Operation(summary = "Create a reply comment for a comment")
    @ApiResponse(responseCode = "201", description = "Successfully created the reply comment")
    @PostMapping("/{parentCommentId}")
    public ResponseEntity<String> createReplyComment(@RequestBody @Validated CommentDto commentDto, @RequestParam("postId") Long postId,
                                                     @RequestParam("username") String username, @RequestParam("userId") Long userId,
                                                     @PathVariable("parentCommentId") Long parentCommentId) {
        CommentDto userComment = commentService.createCommentWithUser(username,userId);
        commentDto.setUsername(userComment.getUsername());
        commentDto.setUserId(userComment.getUserId());
        commentService.createReplyComment(postId, commentDto, parentCommentId);
        return new ResponseEntity<>("Reply comment created", HttpStatus.CREATED);
    }
    @Operation(summary = "Retrieve all comments associated with a specific post ID")
    @ApiResponse(responseCode = "200", description = "Comments successfully retrieved for the given post ID")
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));

    }
    @Operation(summary = "Get comment by id")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved comment by id")
    @GetMapping("/commentId/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("commentId") Long commentId){
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }
    @Operation(summary = "Update a comment in a post")
    @ApiResponse(responseCode = "200", description = "Successfully updated the comment in the post")
    @PutMapping("/{postId}/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId,
                                                    @RequestBody CommentDto commentDto, @RequestParam("username") String username,
                                                    @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(commentService.updateComment(postId, commentId, commentDto, username, userId));
    }
    @Operation(summary = "Delete a comment from a post")
    @ApiResponse(responseCode = "200", description = "Successfully deleted the comment from the post")
    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                @RequestParam("username") String username, @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(commentService.deleteComment(postId, commentId, username, userId));
    }




    }





