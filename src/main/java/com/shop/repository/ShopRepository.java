package com.shop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.Shop;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {

	public Shop findByShopName(String name);

}
