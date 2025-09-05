package org.chanme.be.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
    Optional<User> findByMemberCode(String memberCode);

    boolean existsByPhone(String phone);
    boolean existsByMemberCode(String memberCode);
}
