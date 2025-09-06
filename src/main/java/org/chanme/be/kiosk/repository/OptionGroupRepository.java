package org.chanme.be.kiosk.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.chanme.be.kiosk.domain.option.OptionGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OptionGroupRepository {


    @PersistenceContext
    private EntityManager em;

    public OptionGroup find(Long id) {
        return em.find(OptionGroup.class, id);
    }

    public List<OptionGroup> findByCategory(Long categoryId) {
        String jpql = "SELECT og FROM OptionGroup og WHERE og.category.id = :cid";
        return em.createQuery(jpql, OptionGroup.class)
                .setParameter("cid", categoryId)
                .getResultList();
    }

    /**
     * 특정 아이템 ID와 카테고리 ID에 해당하는 모든 옵션 그룹을
     * 옵션 값(values)과 함께 한 번의 쿼리로 조회합니다.
     */
    public List<OptionGroup> findByItemAndCategoryWithOptions(Long itemId, Long categoryId) {
        // 아이템에 직접 연결된 옵션과, 카테고리에 연결된 옵션을 모두 가져옵니다.
        String jpql = "SELECT DISTINCT og FROM OptionGroup og " +
                "LEFT JOIN FETCH og.values v " +
                "WHERE og.item.id = :itemId OR og.category.id = :categoryId";

        return em.createQuery(jpql, OptionGroup.class)
                .setParameter("itemId", itemId)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    /*

    public void save(OptionGroup og) {
        if (og.getId() == null) em.persist(og);
        else em.merge(og);
    }

    public void delete(Long id) {
        OptionGroup og = em.find(OptionGroup.class, id);
        if (og != null) em.remove(og);
    }

     */
}
