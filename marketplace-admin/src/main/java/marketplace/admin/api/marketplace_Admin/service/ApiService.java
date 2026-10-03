package marketplace.admin.api.marketplace_Admin.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public interface ApiService {
	
	// save the details into manageecom to save marketplace details
	public boolean requestToManageecomToSaveMarketplace(Map<String, String> map);
	
	// save the details into manageecom to save marketplace details
	public boolean requestToManageecomToRemoveMarketplace(Map<String, String> map);

	// verify marketplace details (policymaker api)
	public String verifyMarketplace(Map<String, String> map);
	
	//get marketplace details from policymaker db
	public Map<String,String> getMarketplaceDetails(Map<String, String> map);
}
