package kz.adem.likeservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UsernamesResponse {
    private List<String> usernames;
}
