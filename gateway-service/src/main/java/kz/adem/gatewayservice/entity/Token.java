package kz.adem.gatewayservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user_tokens")
public class Token {

    @Id
    public Long id;

    public String token;

    public String tokenType;

    public boolean revoked;

    public boolean expired;

    public Long userId;
}