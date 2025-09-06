package org.chanme.be.score;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ScoreRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Score score) {
        em.persist(score);
    }

    


}