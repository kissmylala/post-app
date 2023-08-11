package kz.adem.postservice.repository;

import kz.adem.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<List<Post>> findAllByUserId(Long userId);
    Optional<List<Post>> findAllByUsername(String username);
    Optional<Post> findByTitle(String title);



}
