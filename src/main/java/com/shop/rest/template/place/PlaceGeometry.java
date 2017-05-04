package com.shop.rest.template.place;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceGeometry {

	@JsonProperty("location")
	private PlaceLocation location;

	public PlaceLocation getLocation() {
		return location;
	}

	public void setLocation(PlaceLocation location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "PlaceGeometry [location=" + location + "]";
	}

}
