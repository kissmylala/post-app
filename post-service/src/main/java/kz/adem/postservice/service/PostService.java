package kz.adem.postservice.service;

import kz.adem.postservice.dto.PostDto;
import kz.adem.postservice.dto.UsernamesResponse;

import java.util.List;

public interface PostService {
  List<PostDto> getAllPosts();
  List<PostDto> getAllPostsByUserId(Long userId);
  List<PostDto> getAllPostsByUsername(String username);
  PostDto createPost(PostDto postDto);
  PostDto updatePost(Long id, PostDto postDto,String username);
  String deletePostById(Long id,String username);
  PostDto createPostWithUser(PostDto postDto, String username);
  void incrementLikes(Long postId);
  void likePost(Long userId, Long postId);
  PostDto getPostById(Long postId);
  void unlikePost(Long userId, Long postId);
  List<String> getPostLikers(Long postId);
  Boolean existsBydId(Long postId);


}
