package org.chanme.be.domain.friend;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    boolean existsByUserIdAndFriendId(Long userId, Long friendId);
    List<Friendship> findByUserId(Long userId);
}
