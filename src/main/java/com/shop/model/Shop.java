package com.shop.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "shop")
public class Shop {

	@Id
	private String shopName;

	private ShopAddress shopAddress;

	public Shop() {
	}

	public Shop(String shopName, ShopAddress shopAddress) {
		super();
		this.shopName = shopName;
		this.shopAddress = shopAddress;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public ShopAddress getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(ShopAddress shopAddress) {
		this.shopAddress = shopAddress;
	}


}
