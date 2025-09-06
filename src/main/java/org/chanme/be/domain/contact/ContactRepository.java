package org.chanme.be.domain.contact;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    public Contact findByNickname(String name);
}
