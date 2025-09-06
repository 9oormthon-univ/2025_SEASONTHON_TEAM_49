package org.chanme.be.domain.gallary;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GallaryRepository extends JpaRepository<Gallary, Long> {
    public void deleteById(Long id);
}
