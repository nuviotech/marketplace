package com.marketplace.api.serviceClasses;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class ApiCalls {
	private static final Logger logger = LoggerFactory.getLogger(ApiCalls.class);

	@Autowired
	MarketplaceAdminSetting marketplaceAdminSetting;
	
	public String getProductData(String url,String sellerEmail) {
		
		Response response=null;
		try {
			//companyEmail
			String marketplaceEmail=marketplaceAdminSetting.getSettings().getEmail();
			String payload = "{\r\n     \"userId\":\"" + sellerEmail + "\"  , \"marketplaceEmail\": \""+marketplaceEmail+"\"   \r\n}";
			logger.info("### TRACE ### [ApiCalls.getProductData] Calling URL: {}", url);
			logger.info("### TRACE ### Method: POST");
			logger.info("### TRACE ### Payload: {}", payload);

			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json");
			okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, payload);
			Request request = new Request.Builder().url(url).method("POST", body)
					.addHeader("Content-Type", "application/json")
					.build();
			response = client.newCall(request).execute();
			logger.info("### TRACE ### [ApiCalls.getProductData] Status: {}", response.code());
			String resStr = response.body().string();
			logger.info("### TRACE ### [ApiCalls.getProductData] Response: {}", resStr.length() > 300 ? resStr.substring(0, 300) + "...(truncated)" : resStr);
			return resStr;
		}catch(Exception e) {
			logger.error("### TRACE ### [ApiCalls.getProductData] Error: {}", e.getMessage());
			e.printStackTrace();
			return "[]";
		}finally {
			if(response!=null)
			response.close();
		}
	}
	
	//update rating ration in manageecom
	public String updateRatingRatio(String url,Map<String,Object> map) {
		
		Response response=null;
		try {
			String payload = "{\r\n     \"productId\":\"" +map.get("productId") + "\"  , \"rating\": \""+map.get("rating")+"\", \"incrementBy\":\""+map.get("increment")+"\"   \r\n}";
			logger.info("### TRACE ### [ApiCalls.updateRatingRatio] Calling URL: {}", url);
			logger.info("### TRACE ### Method: POST");
			logger.info("### TRACE ### Payload: {}", payload);

			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json");
			okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, payload);
			Request request = new Request.Builder().url(url).method("POST", body)
					.addHeader("Content-Type", "application/json")
					.build();
			response = client.newCall(request).execute();
			logger.info("### TRACE ### [ApiCalls.updateRatingRatio] Status: {}", response.code());
			String resStr = response.body().string();
			logger.info("### TRACE ### [ApiCalls.updateRatingRatio] Response: {}", resStr);
			return resStr;
		}catch(Exception e) {
			logger.error("### TRACE ### [ApiCalls.updateRatingRatio] Error: {}", e.getMessage());
			e.printStackTrace();
			return "[]";
		}finally {
			if(response!=null) response.close();
		}
	}
}
