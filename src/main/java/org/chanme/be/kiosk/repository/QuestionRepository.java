package org.chanme.be.kiosk.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.chanme.be.kiosk.domain.KioskType;
import org.chanme.be.kiosk.domain.qusetion.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionRepository {

    @PersistenceContext
    private EntityManager em;

    public Question find(Long id) {
        // fetch join 으로 items·options 한 번에 가져오기
        String jpql = """
            SELECT q
              FROM Question q
              LEFT JOIN FETCH q.items qi
              LEFT JOIN FETCH qi.options qo
             WHERE q.id = :qid
        """;
        return em.createQuery(jpql, Question.class)
                .setParameter("qid", id)
                .getSingleResult();
    }

    /**
     * 주어진 KioskType에 해당하는 모든 문제를 조회합니다.
     */
    public List<Question> findByKioskType(KioskType kioskType) {
        String jpql = "SELECT q FROM Question q WHERE q.kioskType = :kt";
        return em.createQuery(jpql, Question.class)
                .setParameter("kt", kioskType)
                .getResultList();
    }




    /*
    public void save(Question q) {
        if (q.getId() == null) em.persist(q);
        else em.merge(q);
    }

    public void delete(Long id) {
        Question q = em.find(Question.class, id);
        if (q != null) em.remove(q);
    }

     */
}
