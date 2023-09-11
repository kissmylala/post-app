package kz.adem.gatewayservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LoginDto {
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 4, max = 16, message = "Username should be between 4 and 16 characters")
    private String username;
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, max = 50, message = "Password should be between 8 and 50 characters")
    private String password;
}
