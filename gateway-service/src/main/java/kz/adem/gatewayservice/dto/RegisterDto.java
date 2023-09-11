package kz.adem.gatewayservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RegisterDto {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    private String name;
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 4, max = 16, message = "Username should be between 4 and 16 characters")
    private String username;
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, max = 50, message = "Password should be between 8 and 50 characters")
    private String password;
    private boolean enabled;
}
