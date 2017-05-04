package com.shop.rest.template.place;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDetailsResponse {

	@JsonProperty("results")
	private PlaceDetails[] result;

	
	public PlaceDetails[] getResult() {
		return result;
	}
	
	public void setResult(PlaceDetails[] result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "PlaceDetailsResponse [result=" + result + "]";
	}
	
	
}
