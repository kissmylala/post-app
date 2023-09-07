package kz.adem.postservice.repository;

import kz.adem.postservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findByUserIdAndPostId(Long userId, Long postId);
    List<Like> findAllByPostId(Long postId);
}
