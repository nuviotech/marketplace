package marketplace.admin.api.marketplace_Admin.Implementation;

import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import marketplace.admin.api.marketplace_Admin.service.ApiService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class ApiServiceImpl implements ApiService {

	private static final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);

	@Value("${manageEcom.api.url}")
	String manageEcomApiUrl;
	
	@Value("${apiKey}")
	String apiKey;

	@Value("${policyMaker.url}")
	private String policymakerApiUrl;
	
	
	private Response getResponse(String url,Map<String,String> map) {
		try {
			OkHttpClient httpClient = new OkHttpClient();
			ObjectMapper objectMapper = new ObjectMapper();

			String jsonPayload = objectMapper.writeValueAsString(map);
			logger.info("### TRACE ### [ApiServiceImpl.getResponse] Calling URL: {}", url);
			logger.info("### TRACE ### Method: POST");
			logger.info("### TRACE ### Payload: {}", jsonPayload);

			okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
					jsonPayload);

			// save marketplace details in manageecom
			Request req = new Request.Builder().url(url).post(body)
					.addHeader("apikey", apiKey)
					.build();
			
			Response resp = httpClient.newCall(req).execute();
			logger.info("### TRACE ### [ApiServiceImpl.getResponse] Status: {}", resp != null ? resp.code() : "null");
			return resp;
		}catch(Exception e) {
			logger.error("### TRACE ### [ApiServiceImpl.getResponse] Error: {}", e.getMessage());
			System.err.println("URL : "+url+" | "+map);
			e.printStackTrace();
			return null;
		}
	}

	// save the details into manageecom to save marketplace details
	public boolean requestToManageecomToSaveMarketplace(Map<String, String> map) {
		try {
//			OkHttpClient httpClient = new OkHttpClient();
//			ObjectMapper objectMapper = new ObjectMapper();
//
//			okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
//					objectMapper.writeValueAsString(map));
//
//			// save marketplace details in manageecom
//			Request req = new Request.Builder().url(manageEcomApiUrl + "/saveMarketplaceDetails").post(body)
//					.addHeader("apikey", apiKey)
//					.build();
//			
//			Response resp = httpClient.newCall(req).execute();
			Response resp =getResponse(manageEcomApiUrl + "/saveMarketplaceDetails",map);
			if (resp!=null && resp.isSuccessful()) {
				resp.close();
				return true;
			} else {
				throw new IOException("Unexpected code " + resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// save the details into manageecom to save marketplace details
	public boolean requestToManageecomToRemoveMarketplace(Map<String, String> map) {
		try {
//			OkHttpClient httpClient = new OkHttpClient();
//			ObjectMapper objectMapper = new ObjectMapper();
//
//			okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
//					objectMapper.writeValueAsString(map));
//
//			// save marketplace details in manageecom
//			Request req = new Request.Builder().url(manageEcomApiUrl + "/removeMarketplaceDetails").post(body)
//					.addHeader("apikey", apiKey)
//					.build();
//			Response resp = httpClient.newCall(req).execute();
			Response resp =getResponse(manageEcomApiUrl + "/removeMarketplaceDetails", map);
			if (resp!=null && resp.isSuccessful()) {
				resp.close();
				return true;
			} else {
				throw new IOException("Unexpected code " + resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// verify marketplace details (policymaker api)
	public String verifyMarketplace(Map<String, String> map) {
		try {
//			OkHttpClient httpClient = new OkHttpClient();
//			ObjectMapper objectMapper = new ObjectMapper();
//
//			okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
//					objectMapper.writeValueAsString(map));
//
//			// save marketplace details in manageecom
//			Request req = new Request.Builder().url(policymakerApiUrl + "/verifyMarketplaceAdminRecord").post(body)
//					.addHeader("apikey", apiKey)
//					.build();
//			
//			Response resp = httpClient.newCall(req).execute();
			Response resp = getResponse(policymakerApiUrl + "/verifyMarketplaceAdminRecord", map);
			if (resp!=null && resp.isSuccessful()) {
				String verifyres=resp.body().string();
				resp.close();
				return verifyres;
			} else {
				throw new IOException("Unexpected code " + resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// get marketplace details (policymaker api)
		public Map<String,String> getMarketplaceDetails(Map<String, String> map) {
			try {
//				OkHttpClient httpClient = new OkHttpClient();
//				ObjectMapper objectMapper = new ObjectMapper();
//
//				okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
//						objectMapper.writeValueAsString(map));
//
//				// save marketplace details in manageecom
//				Request req = new Request.Builder().url(policymakerApiUrl + "/getMarketplaceDetails").post(body)
//						.addHeader("apikey", apiKey)
//						.build();
//				
//				Response resp = httpClient.newCall(req).execute();
				Response resp= getResponse(policymakerApiUrl + "/getMarketplaceDetails", map);
				if (resp!=null && resp.isSuccessful()) {
					String verifyres=resp.body().string();
					resp.close();
					JSONObject obj=new JSONObject(verifyres);
					if(obj !=null && obj.getInt("status")==0) {
						ObjectMapper objectMapper = new ObjectMapper();
						Map<String, String> resMap = objectMapper.readValue(obj.get("marketplace_data")+"", Map.class);
						resMap.put("status","0");
						return resMap;
					}else if(obj !=null) {
						throw new Exception(obj.getString("message"));
					}
					return null;
				} else {
					throw new IOException("Unexpected code " + resp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Map.of("status","-1","message",e.getMessage());
			}
		}
}
