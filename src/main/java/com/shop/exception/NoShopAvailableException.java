package com.shop.exception;

public class NoShopAvailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoShopAvailableException(String message) {
		super(message);
	}

	public NoShopAvailableException() {
	}

}
