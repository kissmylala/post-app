package kz.adem.commentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="POST-SERVICE")
public interface PostClient {
    @GetMapping("/api/v1/posts/{postId}/exists")
    Boolean existsById(@PathVariable(value = "postId") Long postId);
}
