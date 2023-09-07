package kz.adem.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.postservice.dto.PostDto;
import kz.adem.postservice.dto.UserDto;
import kz.adem.postservice.service.LikeService;
import kz.adem.postservice.service.PostService;
import kz.adem.util.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class LikeController {
    private final PostService postService;
    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostDto> likePost(@PathVariable(value = "postId") Long postId, HttpServletRequest request){
        Long userId = ControllerUtils.getUserIdFromRequest(request);
        likeService.likePost(userId,postId);
        PostDto likedPost = postService.getPostById(postId);
        return new ResponseEntity<>(likedPost, HttpStatus.OK);
    }
    @PostMapping("/{postId}/unlike")
    public ResponseEntity<PostDto> unlikePost(@PathVariable(value = "postId") Long postId,HttpServletRequest request){
        Long userId = ControllerUtils.getUserIdFromRequest(request);
        likeService.unlikePost(userId,postId);
        PostDto likedPost = postService.getPostById(postId);
        return new ResponseEntity<>(likedPost,HttpStatus.OK);
    }
    @GetMapping("/{postId}/likers")
    public ResponseEntity<List<UserDto>> getPostLikers(@PathVariable(value = "postId") Long postId){
        List<UserDto> postLikers = likeService.getUsersWhoLikedPost(postId);
        return new ResponseEntity<>(postLikers,HttpStatus.OK);
    }
}
