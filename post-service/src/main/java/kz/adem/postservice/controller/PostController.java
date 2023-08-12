package kz.adem.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
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
        String username = request.getHeader("user");

        PostDto createdPost = postService.createPostWithUser(postDto,username);
        return new ResponseEntity<>(createdPost,HttpStatus.CREATED);
    }
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(value = "postId") Long postId,
                                              @RequestBody PostDto postDto,HttpServletRequest request){
        String username = request.getHeader("user");
        if (!StringUtils.hasText(username)){
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        PostDto updatedPost = postService.updatePost(postId,postDto,username);
        return new ResponseEntity<>(updatedPost,HttpStatus.OK);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable(value = "postId") Long postId,HttpServletRequest request){
        String username = request.getHeader("user");
        if (!StringUtils.hasText(username)){
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        String response = postService.deletePostById(postId,username);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/{postId}/like")
    public ResponseEntity<PostDto> likePost(@PathVariable(value = "postId") Long postId,HttpServletRequest request){
        String username = request.getHeader("user");
        if (!StringUtils.hasText(username)){
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        Long userId = Long.parseLong(request.getHeader("user_id"));
        postService.likePost(userId,postId);
        PostDto likedPost = postService.getPostById(postId);
        return new ResponseEntity<>(likedPost,HttpStatus.OK);
    }


}
