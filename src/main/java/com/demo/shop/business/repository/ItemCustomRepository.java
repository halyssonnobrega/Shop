package com.demo.shop.business.repository;

import com.demo.shop.business.entity.ItemEO;

import java.util.List;

public interface ItemCustomRepository {

    List<ItemEO> findItensByFilters(Long id, String name, Double price);
}
