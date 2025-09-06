package org.chanme.be.kiosk.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.chanme.be.kiosk.domain.Category;
import org.chanme.be.kiosk.domain.KioskType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    public Category find(Long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findByKioskType(KioskType kioskType) {
        String jpql = "SELECT c FROM Category c WHERE c.kioskType = :kt";
        TypedQuery<Category> q = em.createQuery(jpql, Category.class);
        q.setParameter("kt", kioskType);
        return q.getResultList();
    }

    public void save(Category category) {
        if (category.getId() == null) {
            em.persist(category);
        } else {
            em.merge(category);
        }
    }

    public void delete(Long id) {
        Category c = em.find(Category.class, id);
        if (c != null) em.remove(c);
    }

}
