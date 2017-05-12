package com.shop.rest.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shop.model.Shop;
import com.shop.service.shops.ShopService;

/**
 * REST Controller responsible for receiving requests from client side
 * 
 * @author fobm
 *
 */
@RestController
public class ShopController {

	@Autowired
	ShopService shopService;

	@Autowired
	RequestMappingHandlerMapping requestMappingHandlerMapping;

	/**
	 * Default end-point of API Anything that does not match with available
	 * end-points will be redirected to findAllShops
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET)
	void defaultRedirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/findAllShops");
	}

	/**
	 * Simple end-point that returns all shops records in database
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findAllShops", method = RequestMethod.GET)
	public @ResponseBody Iterable<Shop> findAll() {
		return shopService.findAll();
	}

	/**
	 * Receives a json object of shop which will be inserted or updated
	 * 
	 * @param shop
	 * @return ObjectNode containing shop information
	 * @throws IOException
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/shops", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ObjectNode newShop(HttpServletResponse response,
			@RequestBody Shop shop) throws IOException {

		if (isInvalid(shop)) {
			response.sendError(400, "Invalid parameters");
			// return statement after calling sendError to prevent further
			// processing.
			return null;
		}

		return shopService.saveShop(shop);
	}

	/**
	 * Returns the nearest shop information which will be calculated through
	 * provided @latitude and @longitude parameters
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws IOException
	 * */
	@RequestMapping(value = "/findNearest", method = RequestMethod.GET)
	public ObjectNode findNearest(HttpServletResponse response,
			@RequestParam(value = "latitude", required = true) String latitude,
			@RequestParam(value = "longitude", required = true) String longitude)
			throws IOException {

		if (latitude.isEmpty() || latitude.isEmpty()) {
			response.sendError(400, "Empty parameter not allowed");
			// return statement after calling sendError to prevent further
			// processing.
			return null;
		}

		return shopService.findNearestShop(latitude, longitude);
	}

	/**
	 * Verify if Shop object contains invalid parameters
	 * 
	 * @param shop
	 * @return
	 */
	private boolean isInvalid(Shop shop) {

		boolean isInvalid = false;

		if (shop.getShopName() == null || shop.getShopName().isEmpty()
				|| shop.getShopName() == null || shop.getShopAddress() == null
				|| shop.getShopAddress().getNumber() == null
				|| shop.getShopAddress().getNumber().isEmpty()
				|| shop.getShopAddress().getPostCode() == null
				|| shop.getShopAddress().getPostCode().isEmpty()) {

			isInvalid = true;
		}

		return isInvalid;
	}
}
