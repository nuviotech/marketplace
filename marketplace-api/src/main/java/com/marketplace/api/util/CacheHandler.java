package com.marketplace.api.util;

import java.net.ConnectException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.marketplace.api.entitys.Seller;
import com.marketplace.api.repository.SellerRepository;
import com.marketplace.api.serviceClasses.ApiCalls;

@Component
public class CacheHandler {
	@Autowired
	SellerRepository sellerRepository;
	@Autowired
    ApiCalls apiCalls;
	
	Logger logger=LoggerFactory.getLogger(CacheHandler.class);
	static public HashMap<String, String> products = new HashMap<>();

	public static HashMap<String, String> getSellerProductCaches() {
		return products;
	}

	public static void setSellerProductCaches(HashMap<String, String> sellerProductCaches) {
		CacheHandler.products = sellerProductCaches;
	}

	//get product from manageecom
	public String initProducts() {
		try {
			// get the products from active sellers
			for (Seller s : sellerRepository.findByStatus("active")) {
				var url = s.getApiEndpoint() + "/getProducts";
				String string = apiCalls.getProductData(url, s.getEmailAddr());
				JSONArray jsonArray = new JSONArray(string);
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsnobject = new JSONObject(jsonArray.get(i).toString());
						jsnobject.put("companyName", s.getNameOfCompany());
						String productRefId = jsnobject.getString("productRefId");
						CacheHandler.products.put(productRefId, jsnobject.toString());
					}
				}
			}
		return "Cache count : " + CacheHandler.products.size();
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

    @Scheduled(cron = "0 0 1 * * *") // At 1:00 AM every night
	public void setUpData() {
		try {
			logger.info("update chache scheduler is call..");
			CacheHandler.products.clear();
			for (Seller s : sellerRepository.findByStatus("active")) {
				var url = s.getApiEndpoint() + "/getProducts";
				String res = apiCalls.getProductData(url, s.getEmailAddr());
				
				JSONArray jsonArray = new JSONArray(res);
				if (jsonArray != null) {
					// Iterating JSON array
					
					for(int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsnobject = new JSONObject(jsonArray.get(i).toString());
						// jsnobject.append("companyName", s.getNameOfCompany());
						jsnobject.put("companyName", s.getNameOfCompany());
						String productRefId = jsnobject.getString("productRefId");
						CacheHandler.products.put(productRefId, jsnobject.toString());
					}
				}
			}
			logger.info("CACHE UPDATED");
			//return "cache updated.";
		}catch(Exception e) {
			logger.error("UPDATE_CACHE_ERROR : "+e.getMessage());
			e.printStackTrace();
			//return e.getMessage();
		}
	}
}
