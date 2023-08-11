package kz.adem.postservice.service.impl;

import kz.adem.postservice.dto.PostDto;
import kz.adem.postservice.dto.UserDto;
import kz.adem.postservice.entity.Post;
import kz.adem.postservice.exception.ResourceNotFoundException;
import kz.adem.postservice.exception.UnauthorizedAccessException;
import kz.adem.postservice.mapper.PostMapper;
import kz.adem.postservice.repository.PostRepository;
import kz.adem.postservice.service.PostService;
import kz.adem.postservice.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserClient userClient;
    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream().map(PostMapper.MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAllPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findAllByUserId(userId).
                orElseThrow(()-> new ResourceNotFoundException("Posts","user id",String.valueOf(userId)));

        return posts.stream()
                .map(PostMapper.MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto createPostWithUser(PostDto postDto, String username) {
        if (StringUtils.hasText(username)){
            postDto.setUsername(username);
            UserDto userDto = userClient.getUserByUsername(username);
            if(userDto==null){
                throw new ResourceNotFoundException("User","username",username);
            }
            postDto.setUserId(userDto.getId());
        }
        return createPost(postDto);
    }



    @Override
    public List<PostDto> getAllPostsByUsername(String username) {
        List<Post> posts = postRepository.findAllByUsername(username).
                orElseThrow(()-> new ResourceNotFoundException("Posts","username",username));

        return posts.stream()
                .map(PostMapper.MAPPER::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public PostDto createPost(PostDto postDto) {
        Post newPost = PostMapper.MAPPER.mapToEntity(postDto);
        return PostMapper.MAPPER.mapToDto(postRepository.save(newPost));
    }

    @Override
    public PostDto updatePost(Long id, PostDto postDto,String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",String.valueOf(id)));
        if (!post.getUsername().equals(username)){
            throw new UnauthorizedAccessException("User is not authorized to update this post");
        }
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setTitle(postDto.getTitle());
        return PostMapper.MAPPER.mapToDto(postRepository.save(post));
    }

    @Override
    public String deletePostById(Long id,String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",String.valueOf(id)));
        if (!post.getUsername().equals(username)){
            throw new UnauthorizedAccessException("User is not authorized to delete this post");
        }
        postRepository.deleteById(id);
        return "Post with id "+id+" successfully deleted!";
    }
}
