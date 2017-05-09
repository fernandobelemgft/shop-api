package com.shop.service.distance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.shop.exception.LocationNotFoundException;
import com.shop.model.Shop;
import com.shop.model.ShopAddress;
import com.shop.service.distance.impl.DistanceMatrixServiceImpl;
import com.shop.service.shops.ShopService;
import com.shop.service.shops.impl.ShopServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({ ShopServiceImpl.class, DistanceMatrixServiceImpl.class })
public class DistanceMatrixServiceTest {

	@Autowired
	DistanceMatrixService distanceMatrix;

	@Autowired
	ShopService shopService;

	List<Shop> shopList;

	@Before
	public void insertDummyData() throws LocationNotFoundException {
		shopService.eraseDatabase();
		shopService.saveShop(new Shop("Birmingham Shop", new ShopAddress("1",
				"B1 1BA")));
		shopService.saveShop(new Shop("Derby Shop", new ShopAddress("1",
				"DE1 0GR")));
		shopService.saveShop(new Shop("Oxford Shop", new ShopAddress("1",
				"OX1 1AA")));
		shopList = shopService.findAll();
	}

	@Test
	public void getNearestTest() throws LocationNotFoundException {

		assertThat(shopList.size()).isEqualTo(3);

		// Client geolocation from: Ryecote Lane, Milton Common OX9 2PU, United
		// Kingdom
		Shop shop = distanceMatrix.getNearest(shopList, "51.7279805",
				"-1.0259525");

		// nearest shop should be Oxford Shop
		assertThat(shop.getShopName()).isEqualTo("Oxford Shop");

		// Client geolocation from: Uttoxeter Rd, Derby DE22 3NE, UK
		Shop shop2 = distanceMatrix.getNearest(shopList, "52.911486",
				"-1.5124852");

		// nearest shop should be Derby Shop
		assertThat(shop2.getShopName()).isEqualTo("Derby Shop");

	}

	@Test(expected = LocationNotFoundException.class)
	public void getNearestShopInvalidLocation()
			throws LocationNotFoundException {
		distanceMatrix.getNearest(shopList, "", "");
	}

	@Test(expected = LocationNotFoundException.class)
	public void getNearestShopInvalidLocationData()
			throws LocationNotFoundException {
		distanceMatrix.getNearest(shopList, "error", "error");
	}

}
