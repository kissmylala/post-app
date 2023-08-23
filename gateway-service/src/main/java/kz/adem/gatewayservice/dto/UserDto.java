package kz.adem.gatewayservice.dto;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
}
