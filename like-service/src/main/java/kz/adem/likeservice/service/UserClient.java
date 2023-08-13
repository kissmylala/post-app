package kz.adem.likeservice.service;

import kz.adem.likeservice.dto.UsernamesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    @PostMapping("/api/v1/users/liked/usernames")
    List<String> getAllUsernamesByIdIn(List<Long> ids);
}