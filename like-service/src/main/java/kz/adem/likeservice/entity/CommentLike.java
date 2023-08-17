package kz.adem.likeservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comment_likes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","comment_id"}))
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "comment_id",nullable = false)
    private Long commentId;


}