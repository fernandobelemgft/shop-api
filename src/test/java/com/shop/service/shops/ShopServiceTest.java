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

import com.shop.exception.LocationNotFoundException;
import com.shop.model.Shop;
import com.shop.model.ShopAddress;
import com.shop.service.distance.DistanceMatrixService;
import com.shop.service.place.GeolocationService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({ShopService.class, GeolocationService.class, DistanceMatrixService.class})
@DataJpaTest
public class ShopServiceTest {
	
	@Autowired
	ShopService shopService;
	
	@Test
	public void insertShopTest() throws LocationNotFoundException{
		
		//Number of shops in database should be 0
		assertThat(shopService.findAll().size()).isEqualTo(0);
		
		//insert new shop
		shopService.saveShop(new Shop("Newcastle Shop", new ShopAddress("1", "NE1 1AD")));
		
		//retrieves after insertion
		List<Shop> shopList = shopService.findAll();
		
		//assert data
		assertThat(shopList.size()).isEqualTo(1);
		assertThat(shopList.get(0).getShopName()).isEqualTo("Newcastle Shop");
		assertThat(shopList.get(0).getShopAddress().getLongitude()).isEqualTo("-1.6157867");
		assertThat(shopList.get(0).getShopAddress().getLatitude()).isEqualTo("54.9677216");
		
	}
	
	@Test
	public void findNearestShopTest() throws LocationNotFoundException{
		
		//Number of shops in database should be 0
		assertThat(shopService.findAll().size()).isEqualTo(0);
		
		shopService.saveShop(new Shop("Newcastle Shop", new ShopAddress("1", "NE1 2AT")));
		shopService.saveShop(new Shop("Manchester Shop", new ShopAddress("1", "M1 1AD")));
		shopService.saveShop(new Shop("Liverpool Shop", new ShopAddress("1", "L1 0AA")));
		
		//Number of shops in database should be 3
		assertThat(shopService.findAll().size()).isEqualTo(3);
		
		Shop shop = null;

		//Whickham, Newcastle upon Tyne NE16, UK
		shop = shopService.findNearestShop("54.944116", "-1.674552");
		assertThat(shop.getShopName()).isEqualTo("Newcastle Shop");
		
		//Platt Fields Park, Manchester M14 6LA, UK 
		shop = shopService.findNearestShop("53.4504731", "-2.2221518");
		assertThat(shop.getShopName()).isEqualTo("Manchester Shop");
		
		//Belle Vale, Liverpool L25, UK 
	    shop = shopService.findNearestShop("53.3953587", "-2.8629684");
	    assertThat(shop.getShopName()).isEqualTo("Liverpool Shop");

	}
	
	@Test(expected=LocationNotFoundException.class)
	public void findNearestShopWithEmptyShopListTest() throws LocationNotFoundException{
		
		//Number of shops in database should be 0
		shopService.eraseDatabase();
		assertThat(shopService.findAll().size()).isEqualTo(0);
		
		//perform search
		shopService.findNearestShop("54.944116", "-1.674552");
	}

}
