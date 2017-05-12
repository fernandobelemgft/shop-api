package com.shop.service.shops;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shop.model.Shop;

public interface ShopService {

	public ObjectNode saveShop(Shop shop);

	public ObjectNode findNearestShop(String latitude, String longitude);

	public List<Shop> findAll();

	public void eraseDatabase();

}
