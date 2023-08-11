package kz.adem.userservice.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RegisterDto {
    private String name;
    private String username;
    private String email;
    private String password;
    private boolean enabled=true;
}
