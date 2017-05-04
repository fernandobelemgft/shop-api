package com.shop.rest.template.distance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceDetailsResponse {
	
	@JsonProperty("rows")
	private DistanceDetails[] distanceDetails;

	public DistanceDetails[] getDistanceDetails() {
		return distanceDetails;
	}

	public void setDistanceDetails(DistanceDetails[] distanceDetails) {
		this.distanceDetails = distanceDetails;
	}

}
