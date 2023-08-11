package kz.adem.postservice.service;

import kz.adem.postservice.dto.PostDto;

import java.util.List;

public interface PostService {
  List<PostDto> getAllPosts();
  List<PostDto> getAllPostsByUserId(Long userId);
  List<PostDto> getAllPostsByUsername(String username);
  PostDto createPost(PostDto postDto);
  PostDto updatePost(Long id, PostDto postDto,String username);
  String deletePostById(Long id,String username);
  PostDto createPostWithUser(PostDto postDto, String username);

}
