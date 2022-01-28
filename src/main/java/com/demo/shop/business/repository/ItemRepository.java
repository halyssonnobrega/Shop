package com.demo.shop.business.repository;

import com.demo.shop.business.entity.ItemEO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEO, Long> {

}
