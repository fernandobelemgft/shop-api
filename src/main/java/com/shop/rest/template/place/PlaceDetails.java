package com.shop.rest.template.place;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDetails {

	@JsonProperty("formatted_address")
	private String formattedAddress;

	@JsonProperty("geometry")
	private PlaceGeometry geometry;

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public PlaceGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(PlaceGeometry geometry) {
		this.geometry = geometry;
	}

	@Override
	public String toString() {
		return "PlaceDetails [geometry=" + geometry + "]";
	}

}
