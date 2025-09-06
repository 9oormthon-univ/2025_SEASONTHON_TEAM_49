package org.chanme.be.kiosk.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.chanme.be.kiosk.domain.item.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {

    @PersistenceContext
    private EntityManager em;

    public Item find(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findByCategory(Long categoryId) {
        String jpql = "SELECT i FROM Item i WHERE i.category.id = :cid";
        return em.createQuery(jpql, Item.class)
                .setParameter("cid", categoryId)
                .getResultList();
    }

    /* 주석 처리 == 현재 사용 하지 않는 기능
    public void save(Item item) {
        if (item.getId() == null) em.persist(item);
        else em.merge(item);
    }

    public void delete(Long id) {
        Item i = em.find(Item.class, id);
        if (i != null) em.remove(i);
    }


     */
}
