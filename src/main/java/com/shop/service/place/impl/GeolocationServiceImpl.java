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
	
	/**
	 * google api url inject through application.properties file
	 */
	private final String url;

	@Autowired
	public GeolocationServiceImpl(@Value("${api.geolocation.key}") String key, @Value("${api.geolocation.url}") String url) {
		this.key = key;
		this.url = url;
	}

	/**
	 * Returns the {@link PlaceLocation} object which contains the longitude and
	 * latitude data required in Shop object
	 * 
	 * @param postCode
	 * @param number
	 * @return {@link PlaceLocation}
	 * @throws LocationNotFoundException
	 *             when the given information does not retrieve a valid
	 *             PlaceLocation object
	 */
	@Override
	public PlaceLocation getPlaceLocation(String postCode, String number)
			throws LocationNotFoundException {

		String formattedURL = String.format(url, number,
				postCode, key);
		
		PlaceDetailsResponse response = new RestTemplate().getForObject(
				formattedURL, PlaceDetailsResponse.class);
		
		if (response.getResult().length == 0) {
			throw new LocationNotFoundException(
					"Location not found - Post Code: " + postCode
							+ ", number: " + number);
		}

		return response.getResult()[0].getGeometry().getLocation();
	}

}
