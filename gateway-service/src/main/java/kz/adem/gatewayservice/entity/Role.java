package kz.adem.gatewayservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "roles")
@EqualsAndHashCode
public class Role {
    @Id
    private Long id;
    private String name;
}