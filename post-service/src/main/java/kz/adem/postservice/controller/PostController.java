package kz.adem.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.postservice.dto.CommentDto;
import kz.adem.postservice.dto.PostCommentDto;
import kz.adem.postservice.dto.PostDto;

import kz.adem.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kz.adem.util.ControllerUtils.getUserIdFromRequest;
import static kz.adem.util.ControllerUtils.getUsernameFromRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(){
        List<PostDto> allPosts = postService.getAllPosts();
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }
    @GetMapping("/userid/{userId}")
    public ResponseEntity<List<PostDto>> getAllPostsByUserId(@PathVariable(value = "userId") Long userId){
        List<PostDto> posts = postService.getAllPostsByUserId(userId);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<List<PostDto>> getAllPostsByUsername(@PathVariable(value = "username") String username){
        List<PostDto> posts = postService.getAllPostsByUsername(username);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, HttpServletRequest request){
        String username = getUsernameFromRequest(request);

        PostDto createdPost = postService.createPostWithUser(postDto,username);
        return new ResponseEntity<>(createdPost,HttpStatus.CREATED);
    }
    @GetMapping("/{postId}")
    public ResponseEntity<PostCommentDto> getPostById(@PathVariable(value = "postId") Long postId){
        return new ResponseEntity<>(postService.getPostByIdWithComments(postId),HttpStatus.OK);
    }
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(value = "postId") Long postId,
                                              @RequestBody PostDto postDto,HttpServletRequest request){
        String username = getUsernameFromRequest(request);

        PostDto updatedPost = postService.updatePost(postId,postDto,username);
        return new ResponseEntity<>(updatedPost,HttpStatus.OK);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable(value = "postId") Long postId,HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        String response = postService.deletePostById(postId,username);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{postId}/exists")
    public ResponseEntity<Boolean> existsBydId(@PathVariable(value = "postId") Long postId){
        Boolean exists = postService.existsBydId(postId);
        return new ResponseEntity<>(exists,HttpStatus.OK);
    }
    @PostMapping("/{postId}/comments")
    public ResponseEntity<String> createCommentToPost(@RequestBody CommentDto commentDto,
                                                      @PathVariable(value = "postId") Long postId,
                                                      HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        String response = postService.createCommentToPost(commentDto,postId,username,userId);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @PostMapping("/{postId}/comments/{parentCommentId}")
    public ResponseEntity<String> createChildCommentToComment(@RequestBody CommentDto commentDto,
                                                      @PathVariable(value = "postId") Long postId,
                                                      HttpServletRequest request,
                                                      @PathVariable(value = "parentCommentId") Long parentCommentId){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        String response = postService.createReplyComment(commentDto,postId,username,userId,parentCommentId);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    // TODO: 15.08.23 refactor getting username and userid from request
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "commentId") Long commentId,
                                                HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        String response = postService.deleteComment(postId,commentId,username,userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postId") Long postId,
                                                    @PathVariable("commentId") Long commentId,
                                                    @RequestBody CommentDto commentDto,
                                                    HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        CommentDto updatedComment = postService.updateComment(postId,commentId,commentDto,username,userId);
        return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }

    @PutMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<CommentDto> likeComment(@PathVariable(value = "postId") Long postId,
                                                   @PathVariable(value = "commentId") Long commentId,
                                                   HttpServletRequest request){
        Long userId = getUserIdFromRequest(request);
        CommentDto likedComment = postService.likeComment(userId,commentId);
        return new ResponseEntity<>(likedComment,HttpStatus.OK);
    }
    @PutMapping("/{postId}/comments/{commentId}/unlike")
    public ResponseEntity<CommentDto> unlikeComment(@PathVariable(value = "postId") Long postId,
                                                  @PathVariable(value = "commentId") Long commentId,
                                                  HttpServletRequest request){
        Long userId = getUserIdFromRequest(request);
        CommentDto likedComment = postService.unlikeComment(userId,commentId);
        return new ResponseEntity<>(likedComment,HttpStatus.OK);
    }
    @GetMapping("/{postId}/comments/{commentId}/likers")
    public ResponseEntity<List<String>> getCommentLikers(@PathVariable("commentId") Long commentId){
        List<String> commentLikers = postService.getCommentLikers(commentId);
        return new ResponseEntity<>(commentLikers,HttpStatus.OK);
    }




}
