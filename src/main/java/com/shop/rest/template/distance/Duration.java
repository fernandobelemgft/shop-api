package com.shop.rest.template.distance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Duration {
	
	@JsonProperty("text")
	private String text; 
	
	@JsonProperty("value")
	private String value;

}
