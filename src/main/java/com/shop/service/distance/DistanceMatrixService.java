package com.shop.service.distance;

import java.util.List;

import com.shop.exception.LocationNotFoundException;
import com.shop.exception.NoShopAvailableException;
import com.shop.model.Shop;

public interface DistanceMatrixService {

	public Shop getNearest(List<Shop> shops, String originLat, String originLgt)
			throws LocationNotFoundException, NoShopAvailableException;

}
