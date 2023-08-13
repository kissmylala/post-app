package kz.adem.commentservice.repository;

import kz.adem.commentservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<List<Comment>> findCommentsByPostId(Long postId);
    Optional<List<Comment>> findCommentsByParentCommentId(Long parentCommentId);

}
