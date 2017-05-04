package com.shop.service.shops;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.exception.LocationNotFoundException;
import com.shop.model.Shop;
import com.shop.repository.ShopRepository;
import com.shop.rest.template.place.PlaceLocation;
import com.shop.service.distance.DistanceMatrixService;
import com.shop.service.place.GeolocationService;

/**
 * Service class designed to interact with {@link ShopRepository} and fire
 * requests to other required services for example retrieving information 
 * google api services
 * 
 * @author fobm
 *
 */
@Service
public class ShopService {

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	GeolocationService geolocationService;

	@Autowired
	DistanceMatrixService distanceMatrixService;

	public List<Shop> findAll() {
		ArrayList<Shop> shops = (ArrayList<Shop>) shopRepository.findAll();
		return shops;
	}
	
	/**
	 * Insert/Update Shop into database
	 * @param name
	 * @param number
	 * @param postCode
	 * @return operation result through @response variable
	 * @throws LocationNotFoundException 
	 */
	public Shop saveShop(Shop shop) throws LocationNotFoundException {
		
		PlaceLocation place = null;
		
		// retrieves geolocation data from google api
		place = geolocationService.getPlaceLocation(shop.getShopAddress()
				.getPostCode(), shop.getShopAddress().getNumber());

		shop.getShopAddress().setLatitude(place.getLat());
		shop.getShopAddress().setLongitude(place.getLng());
		
		shopRepository.save(shop);
		
		return shop;
	}

	public Shop findByShopName(Shop shop) {
		return shopRepository.findByShopName(shop.getShopName());
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
	public Shop findNearestShop(String latitude, String longitude) throws LocationNotFoundException{
		
		Shop shop = null;
		if(shopRepository.count() != 0){
				shop = distanceMatrixService.getNearest((List<Shop>)shopRepository.findAll(), latitude, longitude);
		} else {
			throw new LocationNotFoundException("There is no Shops in Database");
		}
		
		return shop;
	}
	
	public void eraseDatabase(){
		shopRepository.deleteAll();
	}
	
}
