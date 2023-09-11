package kz.adem.postservice.service.impl;

import kz.adem.postservice.dto.CommentDto;
import kz.adem.postservice.dto.PostCommentDto;
import kz.adem.postservice.dto.PostDto;
import kz.adem.postservice.dto.UserDto;
import kz.adem.postservice.entity.Post;
import kz.adem.postservice.exception.ResourceNotFoundException;
import kz.adem.postservice.feign.CommentClient;
import kz.adem.postservice.mapper.PostMapper;
import kz.adem.postservice.repository.PostRepository;
import kz.adem.postservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentClient commentClient;
    private final PostRepository postRepository;
    @Override
    public PostCommentDto createCommentToPost(CommentDto commentDto, Long postId, String username, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",String.valueOf(postId)));
        commentClient.createComment(commentDto, postId, username, userId);
        PostDto postDto = PostMapper.MAPPER.mapToDto(post);
        List<CommentDto> comments = commentClient.getCommentsByPostId(postId)
                .stream().filter(postComment->postComment.getParentCommentId()==null)
                .collect(Collectors.toList());
        PostCommentDto postCommentDto = PostCommentDto.builder()
                .postDto(postDto)
                .commentDtoList(comments)
                .build();
        return postCommentDto;
    }

    @Override
    public String createReplyComment(CommentDto commentDto, Long postId, String username, Long userId, Long parentCommentId) {
        return commentClient.createReplyComment(commentDto, postId, username, userId, parentCommentId);
    }

    @Override
    public PostCommentDto deleteComment(Long postId, Long commentId, String username, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",String.valueOf(postId)));
        commentClient.deleteComment(postId, commentId, username, userId);
        PostDto postDto = PostMapper.MAPPER.mapToDto(post);
        List<CommentDto> comments = commentClient.getCommentsByPostId(postId)
                .stream().filter(postComment->postComment.getParentCommentId()==null)
                .collect(Collectors.toList());
        PostCommentDto postCommentDto = PostCommentDto.builder()
                .postDto(postDto)
                .commentDtoList(comments)
                .build();
        return postCommentDto;
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto, String username, Long userId) {
        return commentClient.updateComment(postId, commentId, commentDto, username, userId);
    }

    @Override
    public CommentDto likeComment(Long commentId, Long userId) {
        return  commentClient.likeComment(commentId, userId);
    }

    @Override
    public CommentDto unlikeComment(Long commentId, Long userId) {
        return  commentClient.unlikeComment(commentId, userId);
    }

    @Override
    public List<UserDto> getCommentLikers(Long commentId) {
        return commentClient.getCommentLikers(commentId);
    }
}
