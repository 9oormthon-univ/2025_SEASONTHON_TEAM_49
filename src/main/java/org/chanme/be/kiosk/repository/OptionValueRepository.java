package org.chanme.be.kiosk.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.chanme.be.kiosk.domain.option.OptionValue;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OptionValueRepository {

    @PersistenceContext
    private EntityManager em;

    public OptionValue find(Long id) {
        return em.find(OptionValue.class, id);
    }


    public List<OptionValue> findByGroup(Long groupId) {
        String jpql = "SELECT v FROM OptionValue v WHERE v.group.id = :gid";
        return em.createQuery(jpql, OptionValue.class)
                .setParameter("gid", groupId)
                .getResultList();
    }

    /*

    public void save(OptionValue v) {
        if (v.getId() == null) em.persist(v);
        else em.merge(v);
    }

    public void delete(Long id) {
        OptionValue v = em.find(OptionValue.class, id);
        if (v != null) em.remove(v);
    }

     */
}
