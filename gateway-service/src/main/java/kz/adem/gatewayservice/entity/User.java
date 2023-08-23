package kz.adem.gatewayservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    private Date createdAt;
    private Set<Role> roles;
}
