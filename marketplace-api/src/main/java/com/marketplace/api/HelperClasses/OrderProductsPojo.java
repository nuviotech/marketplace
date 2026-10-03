package com.marketplace.api.HelperClasses;

import java.util.Map;

import org.json.JSONObject;

public class OrderProductsPojo {
	float totalPrice;
	Map<String,JSONObject> productMapJson;
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Map<String, JSONObject> getProductMapJson() {
		return productMapJson;
	}
	public void setProductMapJson(Map<String, JSONObject> productMapJson) {
		this.productMapJson = productMapJson;
	}
	
}
