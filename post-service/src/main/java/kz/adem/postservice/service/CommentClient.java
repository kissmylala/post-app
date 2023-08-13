package kz.adem.postservice.service;

import jakarta.ws.rs.Path;
import kz.adem.postservice.dto.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "COMMENT-SERVICE")
public interface CommentClient {
    @PostMapping("/api/v1/comments")
        String createComment (@RequestBody CommentDto commentDto, @RequestParam("postId") Long postId,
                @RequestParam("username") String username, @RequestParam("userId") Long userId);

}
