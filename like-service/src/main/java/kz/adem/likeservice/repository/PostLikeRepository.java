package kz.adem.likeservice.repository;

import kz.adem.likeservice.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    PostLike findByUserIdAndPostId(Long userId, Long postId);
    List<PostLike> findAllByPostId(Long postId);

}
