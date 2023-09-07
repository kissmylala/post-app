package kz.adem.commentservice.repository;

import kz.adem.commentservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findByUserIdAndCommentId(Long userId, Long commentId);
    List<Like> findAllByCommentId(Long commentId);
}
