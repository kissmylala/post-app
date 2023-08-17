package kz.adem.likeservice.repository;

import kz.adem.likeservice.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike findByUserIdAndCommentId(Long userId, Long commentId);
    List<CommentLike> findAllByCommentId(Long commentId);

}
