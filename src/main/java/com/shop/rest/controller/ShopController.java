package com.shop.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shop.exception.LocationNotFoundException;
import com.shop.model.Shop;
import com.shop.service.shops.ShopService;

/**
 * REST Controller responsible for receiving 
 * requests from client side
 * 
 * @author fobm
 *
 */
@RestController
public class ShopController {
	
	@Autowired
	ShopService shopService;
	
	@RequestMapping("/findAllShops")
	public @ResponseBody Iterable<Shop> selectFull() {
		return shopService.findAll();
	}

	/**
	 * Receives a json object of shop which will be
	 * inserted or updated
	 * @param shop
	 * @return ObjectNode containing shop information
	 * @throws LocationNotFoundException 
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/shops", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ObjectNode newShop(@RequestBody Shop shop) throws LocationNotFoundException {

		Shop previousVersion = shopService.findByShopName(shop);
		
		ObjectNode shopNode = new ObjectMapper().createObjectNode();
		
		if(previousVersion != null){
        	ObjectNode previousVersionShopNode = buildNodeObjectForShop(previousVersion);
        	shopNode.putPOJO("previousVersion", previousVersionShopNode);
        }
		
		Shop newShop = shopService.saveShop(shop);
		
		ObjectNode newShopNode = buildNodeObjectForShop(newShop);
		shopNode.setAll(newShopNode);

		return shopNode;
	}
	
	
	/**
	 * Build a json object with the information of Shop object
	 * @param shop
	 * @return {@link ObjectNode} containing shop Information
	 */
	private ObjectNode buildNodeObjectForShop(Shop shop) {
		
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode shopNode = mapper.createObjectNode();
		shopNode.put("shopName", shop.getShopName());
		
		ObjectNode addressNode = mapper.createObjectNode();
		addressNode.put("number", shop.getShopAddress().getNumber());
		addressNode.put("postCode", shop.getShopAddress().getPostCode());
		addressNode.put("latitude", shop.getShopAddress().getLatitude());
		addressNode.put("longitude", shop.getShopAddress().getLongitude());
		shopNode.putPOJO("shopAddress", addressNode);
		return shopNode;
	}
	
	/**
	 * Returns the nearest shop information which will be calculated 
	 * through provided @latitude and  @longitude parameters
	 * 
	 * @param latitude
	 * @param longitude
	 * @return 
	 * @throws LocationNotFoundException 
	 * */
	@RequestMapping("/findNearest")
	public Shop findNearest(
			@RequestParam(value = "latitude") String latitude,
			@RequestParam(value = "longitude") String longitude) throws LocationNotFoundException{
		
		return shopService.findNearestShop(latitude, longitude);
	}
	

}
