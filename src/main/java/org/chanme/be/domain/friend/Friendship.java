package org.chanme.be.domain.friend;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "friendships",
    uniqueConstraints = @UniqueConstraint(name = "uq_friend", columnNames = {"user_id","friend_id"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 나 */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 친구 */
    @Column(name = "friend_id", nullable = false)
    private Long friendId;
}
