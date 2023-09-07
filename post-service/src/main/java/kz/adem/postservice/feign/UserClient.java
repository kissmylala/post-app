package kz.adem.postservice.feign;

import kz.adem.postservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "GATEWAY-SERVICE")
public interface UserClient {
    @GetMapping("api/v1/users/username/{username}")
    UserDto getUserByUsername(@PathVariable(value = "username") String username);

    @PostMapping("/api/v1/users/liked/users")
    List<UserDto> getAllUsersByIdIn(List<Long> ids);
}
