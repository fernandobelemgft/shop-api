package com.shop.service.place.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shop.exception.LocationNotFoundException;
import com.shop.rest.template.place.PlaceDetailsResponse;
import com.shop.rest.template.place.PlaceLocation;
import com.shop.service.place.GeolocationService;

/**
 * 
 * Class designed to attend geolocation requirements provided by google api
 *
 * @author fobm
 * 
 */
@Service
public class GeolocationServiceImpl implements GeolocationService {

	/**
	 * google api key inject through application.properties file
	 */
	private final String key;

	private static final String PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&components=postal_code:%s&key=%s";

	@Autowired
	public GeolocationServiceImpl(@Value("${api.geolocation.key}") String key) {
		this.key = key;
	}

	/**
	 * Returns the {@link PlaceLocation} object which contains the longitude and
	 * latitude data required in Shop object
	 * 
	 * @param postalCode
	 * @param number
	 * @return {@link PlaceLocation}
	 * @throws LocationNotFoundException
	 *             when the given information does not retrieve a valid
	 *             PlaceLocation object
	 */
	@Override
	public PlaceLocation getPlaceLocation(String postalCode, String number)
			throws LocationNotFoundException {

		String formmatedURL = String.format(PLACE_DETAILS_URL, number,
				postalCode, key);

		PlaceDetailsResponse response = new RestTemplate().getForObject(
				formmatedURL, PlaceDetailsResponse.class);

		if (response.getResult().length == 0) {
			throw new LocationNotFoundException(
					"Location not found - Postal code: " + postalCode
							+ ", number: " + number);
		}

		return response.getResult()[0].getGeometry().getLocation();
	}

}
