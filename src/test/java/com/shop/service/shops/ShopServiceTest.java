package com.shop.service.shops;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.shop.model.Shop;
import com.shop.model.ShopAddress;
import com.shop.service.distance.impl.DistanceMatrixServiceImpl;
import com.shop.service.place.impl.GeolocationServiceImpl;
import com.shop.service.shops.impl.ShopServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({ ShopServiceImpl.class, GeolocationServiceImpl.class,
		DistanceMatrixServiceImpl.class })
@DataJpaTest
public class ShopServiceTest {

	@Autowired
	ShopService shopService;

	@Test
	public void insertShopTest() {

		// Number of shops in database should be 0
		assertThat(shopService.findAll().size()).isEqualTo(0);

		// insert new shop
		shopService.saveShop(new Shop("Newcastle Shop", new ShopAddress("1",
				"NE1 1AD")));

		// retrieves after insertion
		List<Shop> shopList = shopService.findAll();

		// assert data
		assertThat(shopList.size()).isEqualTo(1);
		assertThat(shopList.get(0).getShopName()).isEqualTo("Newcastle Shop");
		assertThat(shopList.get(0).getShopAddress().getLongitude()).isEqualTo(
				"-1.6157867");
		assertThat(shopList.get(0).getShopAddress().getLatitude()).isEqualTo(
				"54.9677216");

	}

	@Test
	public void findNearestShopTest() {

		// Number of shops in database should be 0
		assertThat(shopService.findAll().size()).isEqualTo(0);

		shopService.saveShop(new Shop("Newcastle Shop", new ShopAddress("1",
				"NE1 2AT")));
		shopService.saveShop(new Shop("Manchester Shop", new ShopAddress("1",
				"M1 1AD")));
		shopService.saveShop(new Shop("Liverpool Shop", new ShopAddress("1",
				"L1 0AA")));

		// Number of shops in database should be 3
		assertThat(shopService.findAll().size()).isEqualTo(3);

		// Whickham, Newcastle upon Tyne NE16, UK
		String newCastleShop = shopService.findNearestShop("54.944116", "-1.674552").get("shopName").asText();
		assertThat(newCastleShop).isEqualTo("Newcastle Shop");

		// Platt Fields Park, Manchester M14 6LA, UK
		String manchesterShop = shopService.findNearestShop("53.4504731", "-2.2221518").get("shopName").asText();
		assertThat(manchesterShop).isEqualTo("Manchester Shop");

		// Belle Vale, Liverpool L25, UK
		String liverpoolShop = shopService.findNearestShop("53.3953587", "-2.8629684").get("shopName").asText();
		assertThat(liverpoolShop).isEqualTo("Liverpool Shop");

	}

}
