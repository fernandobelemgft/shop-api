package com.shop.service.place;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.shop.exception.LocationNotFoundException;
import com.shop.rest.template.place.PlaceLocation;
import com.shop.service.place.impl.GeolocationServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(GeolocationServiceImpl.class)
public class GeolocationServiceTest {

	@Autowired
	GeolocationService geolocationService;

	@Test
	public void getPlaceLocationTest() throws LocationNotFoundException,
			InterruptedException, ExecutionException {
		PlaceLocation place = geolocationService.getPlaceLocation("SE1 0AA",
				"312");
		assertThat(place.getLat()).isEqualTo("51.498409");
		assertThat(place.getLng()).isEqualTo("-0.1016552");
	}

	@Test(expected = LocationNotFoundException.class)
	public void getPlaceLocationInvalidParametersTest()
			throws LocationNotFoundException {
		geolocationService.getPlaceLocation("error", "0");
	}

}
