package kz.adem.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.postservice.dto.CommentDto;
import kz.adem.postservice.dto.PostCommentDto;
import kz.adem.postservice.dto.PostDto;

import kz.adem.postservice.exception.UnauthorizedAccessException;
import kz.adem.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PutMapping("/{postId}/like")
    public ResponseEntity<PostDto> likePost(@PathVariable(value = "postId") Long postId,HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        postService.likePost(userId,postId);
        PostDto likedPost = postService.getPostById(postId);
        return new ResponseEntity<>(likedPost,HttpStatus.OK);
    }
    @PutMapping("/{postId}/unlike")
    public ResponseEntity<PostDto> unlikePost(@PathVariable(value = "postId") Long postId,HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        Long userId = getUserIdFromRequest(request);
        postService.unlikePost(userId,postId);
        PostDto likedPost = postService.getPostById(postId);
        return new ResponseEntity<>(likedPost,HttpStatus.OK);
    }
    @GetMapping("/{postId}/likers")
    public ResponseEntity<List<String>> getPostLikers(@PathVariable(value = "postId") Long postId){
        List<String> postLikers = postService.getPostLikers(postId);
        return new ResponseEntity<>(postLikers,HttpStatus.OK);
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
    private String getUsernameFromRequest(HttpServletRequest request){
        String username =  request.getHeader("user");
        if (!StringUtils.hasText(username)){
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        return username;
    }
    private Long getUserIdFromRequest(HttpServletRequest request){
        Long userId = Long.parseLong(request.getHeader("user_id"));
        if (userId == null){
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        return userId;
    }



}
