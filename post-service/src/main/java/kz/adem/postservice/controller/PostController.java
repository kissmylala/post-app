package kz.adem.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kz.adem.postservice.dto.PostCommentDto;
import kz.adem.postservice.dto.PostDto;
import kz.adem.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kz.adem.postservice.util.ControllerUtils.getUsernameFromRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
@Tag(
        name = "Post service controller",
        description = "CRUD operations for posts"
)
public class PostController {
    private final PostService postService;


    @Operation(summary = "Retrieve all posts")
    @ApiResponse(responseCode = "200",description = "Successfully retrieved list of all posts. STATUS OK.")
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(){
        List<PostDto> allPosts = postService.getAllPosts();
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve posts by user ID")
    @ApiResponse(responseCode = "200",description = "Successfully retrieved list of posts for a specific user")
    @GetMapping("/userid/{userId}")
    public ResponseEntity<List<PostDto>> getAllPostsByUserId(@PathVariable(value = "userId") Long userId){
        List<PostDto> posts = postService.getAllPostsByUserId(userId);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    @Operation(summary = "Retrieve posts by username")
    @ApiResponse(responseCode = "200",description = "Successfully retrieved list of posts for a specific username")
    @GetMapping("/username/{username}")
    public ResponseEntity<List<PostDto>> getAllPostsByUsername(@PathVariable(value = "username") String username){
        List<PostDto> posts = postService.getAllPostsByUsername(username);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }
    @Operation(summary = "Create a new post")
    @ApiResponse(responseCode = "201",description = "Successfully created a new post")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody @Validated PostDto postDto, HttpServletRequest request){
        String username = getUsernameFromRequest(request);

        PostDto createdPost = postService.createPostWithUser(postDto,username);
        return new ResponseEntity<>(createdPost,HttpStatus.CREATED);
    }
    @Operation(summary = "Retrieve a post by its ID along with its comments")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the post")
    @GetMapping("/{postId}")
    public ResponseEntity<PostCommentDto> getPostById(@PathVariable(value = "postId") Long postId){
        return new ResponseEntity<>(postService.getPostByIdWithComments(postId),HttpStatus.OK);
    }
    @Operation(summary = "Update a specific post")
    @ApiResponse(responseCode = "200", description = "Successfully updated the post")
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(value = "postId") Long postId,
                                              @RequestBody PostDto postDto,HttpServletRequest request){
        String username = getUsernameFromRequest(request);

        PostDto updatedPost = postService.updatePost(postId,postDto,username);
        return new ResponseEntity<>(updatedPost,HttpStatus.OK);
    }
    @Operation(summary = "Delete a specific post")
    @ApiResponse(responseCode = "200", description = "Successfully deleted the post")
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable(value = "postId") Long postId,HttpServletRequest request){
        String username = getUsernameFromRequest(request);
        String response = postService.deletePostById(postId,username);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @Operation(summary = "Check if a post exists by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully checked the existence of the post")
    @GetMapping("/{postId}/exists")
    public ResponseEntity<Boolean> existsBydId(@PathVariable(value = "postId") Long postId){
        Boolean exists = postService.existsBydId(postId);
        return new ResponseEntity<>(exists,HttpStatus.OK);
    }





}
