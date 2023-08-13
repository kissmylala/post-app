package kz.adem.commentservice.service;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.commentservice.dto.CommentDto;

import java.util.List;

public interface CommentService {
    //напиши мне метод для создания комментария
    void createComment(Long postId, CommentDto commentDto);
    CommentDto createCommentWithUser(String username, Long userId);
    List<CommentDto> getCommentsByPostId(Long postId);

}
