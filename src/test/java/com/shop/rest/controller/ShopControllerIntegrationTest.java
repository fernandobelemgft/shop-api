package com.shop.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shop.model.Shop;
import com.shop.model.ShopAddress;
import com.shop.service.shops.ShopService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShopControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ShopService service;

	@Test
	public void createShop() {

		service.eraseDatabase();

		assertThat(service.findAll().size()).isZero();

		ObjectMapper mapper = new ObjectMapper();

		ObjectNode shopNode = mapper.createObjectNode();
		shopNode.put("shopName", "Oxford Shop");

		ObjectNode addressNode = mapper.createObjectNode();
		addressNode.put("number", "1");
		addressNode.put("postCode", "OX1 1AA");
		shopNode.putPOJO("shopAddress", addressNode);

		ResponseEntity<Shop> responseEntity = restTemplate.postForEntity(
				"/shops", shopNode, Shop.class);
		Shop shop = responseEntity.getBody();

		assertThat(responseEntity.getStatusCode())
				.isEqualTo(HttpStatus.CREATED);
		assertThat(service.findAll().size()).isEqualTo(1);
		assertThat(shop.getShopName()).isEqualTo("Oxford Shop");

	}

	@Test
	public void findNearest() {

		service.eraseDatabase();
		insertDummyData();

		ResponseEntity<ObjectNode> responseEntity = restTemplate.getForEntity(
				"/findNearest?latitude=51.7279805&longitude=-1.0259525",
				ObjectNode.class);

		Shop shop = convertJsonToShop(responseEntity.getBody());
		assertThat(shop.getShopName()).isEqualTo("Oxford Shop");

	}

	@Test
	public void findNearestEmptyDatabase() {

		service.eraseDatabase();

		ResponseEntity<ObjectNode> responseEntity = restTemplate.getForEntity(
				"/findNearest?latitude=51.7279805&longitude=-1.0259525",
				ObjectNode.class);

		String response = responseEntity.getBody().get("message").asText();
		assertThat(response).isEqualTo("There is no shops available in Database.");
	}

	@Test
	public void findNearestInvalidParameters() {

		service.eraseDatabase();
		insertDummyData();

		ResponseEntity<ObjectNode> responseEntity = restTemplate.getForEntity(
				"/findNearest?latitude=XYZ&longitude=XYZ", ObjectNode.class);

		String response = responseEntity.getBody().get("message").asText();
		assertThat(response).isEqualTo("Your device location is not clear. System cannot provide nearest Shop from you");
	}

	@Test
	public void findNearestEmptyParameters() {

		service.eraseDatabase();

		ResponseEntity<Shop> responseEntity = restTemplate.getForEntity(
				"/findNearest?latitude=&longitude=", Shop.class);

		Shop shop = responseEntity.getBody();
		assertThat(shop.getShopName()).isNull();
		assertThat(shop.getShopAddress()).isNull();

		assertThat(responseEntity.getStatusCode()).isEqualTo(
				HttpStatus.BAD_REQUEST);

	}

	@Test
	public void findAll() {

		service.eraseDatabase();
		insertDummyData();

		Shop[] shops = restTemplate.getForObject("/findAllShops", Shop[].class);

		assertThat(shops.length).isEqualTo(3);

	}

	@Test
	public void findAllEmptyDatabase() {

		service.eraseDatabase();

		Shop[] shops = restTemplate.getForObject("/findAllShops", Shop[].class);

		assertThat(shops.length).isEqualTo(0);

	}

	private void insertDummyData() {
		service.saveShop(new Shop("Newcastle Shop", new ShopAddress("1",
				"NE1 1AD")));
		service.saveShop(new Shop("Derby Shop", new ShopAddress("1", "DE1 0GR")));
		service.saveShop(new Shop("Oxford Shop",
				new ShopAddress("1", "OX1 1AA")));
	}
	
	private Shop convertJsonToShop(ObjectNode on) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.treeToValue(on, Shop.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
