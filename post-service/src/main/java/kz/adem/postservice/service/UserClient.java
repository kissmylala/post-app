package kz.adem.postservice.service;

import kz.adem.postservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    @GetMapping("api/v1/users/username/{username}")
    UserDto getUserByUsername(@PathVariable(value = "username") String username);
}
