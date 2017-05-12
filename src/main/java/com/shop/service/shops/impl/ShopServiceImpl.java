package com.shop.service.shops.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shop.exception.LocationNotFoundException;
import com.shop.exception.NoShopAvailableException;
import com.shop.model.Shop;
import com.shop.repository.ShopRepository;
import com.shop.rest.template.place.PlaceLocation;
import com.shop.service.distance.DistanceMatrixService;
import com.shop.service.place.GeolocationService;
import com.shop.service.shops.ShopService;

/**
 * Service class designed to interact with {@link ShopRepository} and fire
 * requests to other required services for example retrieving information google
 * api services
 * 
 * @author fobm
 *
 */
@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	GeolocationService geolocationService;

	@Autowired
	DistanceMatrixService distanceMatrixService;

	final static Logger logger = Logger.getLogger(ShopServiceImpl.class);

	public List<Shop> findAll() {
		ArrayList<Shop> shops = (ArrayList<Shop>) shopRepository.findAll();
		return shops;
	}

	/**
	 * Insert/Update Shop into database
	 * 
	 * @param name
	 * @param number
	 * @param postCode
	 * @return operation result through @response variable
	 */
	@Override
	public ObjectNode saveShop(Shop shop) {

		//tries to build shop location data inside Shop object
		try {
			shop = buildShopLocation(shop);
		} catch (LocationNotFoundException e) {
			logger.error(e.getMessage());
			return buildNodeObjectForError(e.getMessage());
		}
		
		// create main node for shop response.
		ObjectNode shopNode = new ObjectMapper().createObjectNode();

		// search for an existing shop object in data base .
		Shop previousVersion = shopRepository
				.findByShopName(shop.getShopName());

		// verify if previousVersion != null to create a node of it self inside
		// the response node.
		if (previousVersion != null) {
			ObjectNode previousVersionShopNode = buildNodeObjectForShop(previousVersion);
			shopNode.putPOJO("previousVersion", previousVersionShopNode);
		}


		// create a node for the new shop inside the response node.
		Shop newShop = shopRepository.save(shop);

		// adds new shop to node response
		ObjectNode newShopNode = buildNodeObjectForShop(newShop);
		shopNode.setAll(newShopNode);

		return shopNode;
	}

	/**
	 * Returns the nearest shop information which will be calculated through
	 * provided @latitude and @longitude parameters
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 * */
	@Override
	public ObjectNode findNearestShop(String latitude, String longitude) {

		Shop shop = null;

		try {

			shop = distanceMatrixService.getNearest(
					(List<Shop>) shopRepository.findAll(), latitude, longitude);

			return buildNodeObjectForShop(shop);

		} catch (LocationNotFoundException | NoShopAvailableException e) {
			logger.error(e.getMessage());
			return buildNodeObjectForError(e.getMessage());
		}

	}

	/**
	 * Method created to attend test scenarios
	 */
	@Override
	public void eraseDatabase() {
		shopRepository.deleteAll();
		logger.info("Database erased.");
	}

	/**
	 * Populates current shop object with geolocation information
	 * 
	 * @param shop
	 * @return
	 * @throws LocationNotFoundException
	 */
	private Shop buildShopLocation(Shop shop) throws LocationNotFoundException {

		PlaceLocation place = null;

		// retrieves geolocation data from google api

		place = geolocationService.getPlaceLocation(shop.getShopAddress()
				.getPostCode(), shop.getShopAddress().getNumber());

		shop.getShopAddress().setLatitude(place.getLat());
		shop.getShopAddress().setLongitude(place.getLng());

		return shop;
	}

	/**
	 * Build a json object with the information of Shop object
	 * 
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
	 * Builds a simple node object to provide information for consumer
	 * 
	 * @param message
	 * @return
	 */
	private ObjectNode buildNodeObjectForError(String message) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode messageNode = mapper.createObjectNode();
		messageNode.put("message", message);
		return messageNode;
	}

}
