package com.shop.rest.template.distance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceDetails {

	@JsonProperty("elements")
	private DistanceElement[] distanceElements;

	public DistanceElement[] getDistanceElements() {
		return distanceElements;
	}

	public void setDistanceElements(DistanceElement[] distanceElements) {
		this.distanceElements = distanceElements;
	}

}
