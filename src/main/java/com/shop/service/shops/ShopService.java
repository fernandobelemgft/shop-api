package com.shop.service.shops;

import java.util.List;

import com.shop.exception.LocationNotFoundException;
import com.shop.model.Shop;

public interface ShopService {

	public Shop saveShop(Shop shop) throws LocationNotFoundException;

	public Shop findByShopName(Shop shop);

	public Shop findNearestShop(String latitude, String longitude)
			throws LocationNotFoundException;

	public void eraseDatabase();

	public List<Shop> findAll();

}
