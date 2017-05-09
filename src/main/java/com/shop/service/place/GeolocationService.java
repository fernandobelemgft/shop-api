package com.shop.service.place;

import com.shop.exception.LocationNotFoundException;
import com.shop.rest.template.place.PlaceLocation;

public interface GeolocationService {
	public PlaceLocation getPlaceLocation(String postalCode, String number)
			throws LocationNotFoundException;
}
