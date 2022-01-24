package com.demo.shop.business.repository;

import com.demo.shop.business.entity.ItemEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemCustomRepository {

    @PersistenceContext
    private EntityManager em;

    private static Logger log = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    @Override
    public List<ItemEO> findItensByFilters(Long id, String name, Double price) {
        StringBuilder query = new StringBuilder(" SELECT it FROM ItemEO it ");
        StringBuilder where = new StringBuilder();

        if(id != null && id > 0) {
            where.append(" WHERE it.id =").append(id);
        }

        if(name != null) {
            if(where.toString().isEmpty()) {
                where.append(" WHERE it.name like '%").append(name).append("%'");
            } else {
                where.append(" AND it.name like '%").append(name).append("%'");
            }
        }

        if(price != null && price > 0) {
            if(where.toString().isEmpty()) {
                where.append(" WHERE it.price <=").append(price);
            } else {
                where.append(" AND it.price <=").append(price);
            }
        }

        query.append(where);

        TypedQuery<ItemEO> createQuery = em.createQuery(query.toString(), ItemEO.class);

        return createQuery.getResultList();
    }
}
