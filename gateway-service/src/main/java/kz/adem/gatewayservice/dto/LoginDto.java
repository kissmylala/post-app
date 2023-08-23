package kz.adem.gatewayservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LoginDto {
    private String username;
    private String password;
}
