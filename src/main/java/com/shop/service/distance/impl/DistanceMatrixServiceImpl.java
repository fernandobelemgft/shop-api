package com.shop.service.distance.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shop.exception.LocationNotFoundException;
import com.shop.model.Shop;
import com.shop.rest.template.distance.DistanceDetailsResponse;
import com.shop.rest.template.distance.DistanceElement;
import com.shop.service.distance.DistanceMatrixService;

/**
 * 
 * Class designed to attend distance calculation requirements provided by google
 * api
 *
 * @author fobm
 * 
 */
@Service
public class DistanceMatrixServiceImpl implements DistanceMatrixService {

	/**
	 * google api key inject through application.properties file
	 */
	private final String key;

	private final String DISTANCE_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=%s,%s&destinations=%s,%s&key=%s";

	public DistanceMatrixServiceImpl(@Value("${api.matrix.key}") String key) {
		this.key = key;
	}

	/**
	 * Returns the nearest {@link Shop} according with provided origin
	 * geolocation parameters
	 * 
	 * @param shops
	 * @param originLat
	 * @param originLgt
	 * @return
	 * @throws LocationNotFoundException
	 *             when the given information is not valid to acquire users
	 *             location
	 */
	@Override
	public Shop getNearest(List<Shop> shops, String originLat, String originLgt)
			throws LocationNotFoundException {

		// Map created to attend the necessity of wrapping the Shop object
		// and the DistanceElement that will be provided by the distance matrix
		// api
		Map<Shop, DistanceElement> map = new HashMap<Shop, DistanceElement>();

		if (originLat.isEmpty() || originLgt.isEmpty()) {
			throw new LocationNotFoundException(
					"Device location not detected, you may want to check your location service");
		}

		// for each shop in database a DistanceElement will be retrieved
		// informing the
		// data required to calculate the nearest Shop from client's location
		for (Shop shop : shops) {

			String formmatedURL = String.format(DISTANCE_MATRIX_URL, originLat,
					originLgt, shop.getShopAddress().getLatitude(), shop
							.getShopAddress().getLongitude(), key);

			DistanceDetailsResponse response = new RestTemplate().getForObject(
					formmatedURL, DistanceDetailsResponse.class);

			if (response.getDistanceDetails()[0].getDistanceElements()[0]
					.getDistance() == null) {
				throw new LocationNotFoundException(
						"Your device location is not clear. System cannot provide nearest Shop from you. The available shops are the following:\n"
								+ shops);
			} else {
				map.put(shop,
						response.getDistanceDetails()[0].getDistanceElements()[0]);
			}

		}

		return getNearestShop(map);

	}

	/**
	 * A simple method designed to extract the searching functionality of
	 * finding the option containing the smallest distance value
	 * 
	 * @param map
	 *            created to wrap {@link Shop} and {@link DistanceElement}
	 * @return
	 */
	private Shop getNearestShop(Map<Shop, DistanceElement> map) {

		DistanceElement nearestDistance = null;
		Shop nearestShop = null;

		for (Map.Entry<Shop, DistanceElement> entry : map.entrySet()) {

			if (nearestDistance == null) {
				nearestDistance = entry.getValue();
				nearestShop = entry.getKey();

			} else {
				if (Integer.valueOf(entry.getValue().getDistance().getValue()) < Integer
						.valueOf(nearestDistance.getDistance().getValue())) {
					nearestDistance = entry.getValue();
					nearestShop = entry.getKey();
				}
			}

		}

		return nearestShop;
	}

}
