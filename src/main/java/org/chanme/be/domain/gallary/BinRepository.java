package org.chanme.be.domain.gallary;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BinRepository extends JpaRepository<Bin, Long> {
    public Bin findById(long id);
}
