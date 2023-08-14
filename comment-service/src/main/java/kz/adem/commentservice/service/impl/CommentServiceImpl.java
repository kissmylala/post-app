package kz.adem.commentservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.commentservice.dto.CommentDto;
import kz.adem.commentservice.entity.Comment;
import kz.adem.commentservice.exception.ResourceNotFoundException;
import kz.adem.commentservice.exception.UnauthorizedAccessException;
import kz.adem.commentservice.mapper.CommentMapper;
import kz.adem.commentservice.repository.CommentRepository;
import kz.adem.commentservice.service.CommentService;
import kz.adem.commentservice.service.PostClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostClient postClient;
    @Override
    public void createComment(Long postId, CommentDto commentDto) {
        if (!postClient.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", String.valueOf(postId));
        }

        Comment comment = CommentMapper.MAPPER.mapToEntity(commentDto);
        comment.setPostId(postId);
        comment.setBody(commentDto.getBody());
        commentRepository.save(comment);
    }

    @Override
    public CommentDto createCommentWithUser(String username, Long userId) {
            if (username == null || userId == null) {
                throw new ResourceNotFoundException("User", "username", username);
            }
            CommentDto commentDto = new CommentDto();
            commentDto.setUsername(username);
            commentDto.setUserId(userId);
            return commentDto;
        }


    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","postId",String.valueOf(postId)));
        return comments.stream().filter(comment->comment.getParentComment()==null).map(CommentMapper.MAPPER::mapToDto).toList();
    }

    @Override
    public void createReplyComment(Long postId, CommentDto commentDto, Long parentCommentId) {
        if (!postClient.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", String.valueOf(postId));
        }
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",String.valueOf(parentCommentId)));
        if (parentComment.getParentComment()!=null){
            throw new IllegalArgumentException("Reply comments can only be created for main comments. Nested child comments are not allowed.");
        }
        Comment comment = CommentMapper.MAPPER.mapToEntity(commentDto);
        comment.setPostId(postId);
        comment.setBody(commentDto.getBody());
        comment.setParentComment(commentRepository.findById(parentCommentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",String.valueOf(parentCommentId))));
        comment.setRepliedTo(commentDto.getRepliedTo());
        commentRepository.save(comment);
    }

    @Override
    public String deleteComment(Long postId, Long commentId, String username, Long userId) {
        if (!postClient.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", String.valueOf(postId));
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",String.valueOf(commentId)));
        if (!comment.getUsername().equals(username) || !comment.getUserId().equals(String.valueOf(userId))){
            throw new UnauthorizedAccessException("You are not allowed to delete this comment");
        }
        commentRepository.deleteById(commentId);
        return "Comment successfully deleted";
    }
}
