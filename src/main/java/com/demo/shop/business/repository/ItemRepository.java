package com.demo.shop.business.repository;

import com.demo.shop.business.entity.ItemEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEO, Long> {

    @Query("SELECT item FROM ItemEO item WHERE item.id = :id and item.name = :name")
    List<ItemEO> findByIdAndName(@Param("id") long id, @Param("name") String name);

    @Query(value = "SELECT item FROM ItemEO item WHERE item.name like %:name%")
    List<ItemEO> findByName(@Param("name") String name);
}
