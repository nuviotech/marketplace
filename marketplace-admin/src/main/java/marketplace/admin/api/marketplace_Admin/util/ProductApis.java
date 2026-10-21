package marketplace.admin.api.marketplace_Admin.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import kotlin.collections.ArrayDeque;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class ProductApis {
	@Autowired
	OkHttpClient okHttpClient;
	
	@Value("${manageEcom.api.url}")
	String MANAGEECOM_API_BASE_URL;
	
	Logger logger =LoggerFactory.getLogger(ProductApis.class);
	
	public long getProductCountBySellerId(String sellerId)throws IOException {
	    //OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("sellerId", sellerId);
        String json = objectMapper.writeValueAsString(dataMap);

        // Prepare request body
        RequestBody requestBody = RequestBody.create(json, JSON);

        // Create request
        Request request = new Request.Builder()
                .url(MANAGEECOM_API_BASE_URL+"/api/products/getProductCountBySellerEmailId")
                .post(requestBody)
                .build();

        // Execute request
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
                
            }
            return  Long.parseLong(response.body().string());
        }catch(Exception e) {
        	logger.error("ERROR : "+e.getMessage());
        	e.printStackTrace();
        	return -1;
        }		
    }
	
	public String getProductsBySellerId(String sellerId, int pageNo, int pageSize)throws IOException {
	    //OkHttpClient client = new OkHttpClient();
        List<Products> products=new ArrayList<>(pageSize);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("sellerId", sellerId);
        dataMap.put("page", pageNo);
        dataMap.put("limit",pageSize);
        String json = objectMapper.writeValueAsString(dataMap);

        // Prepare request body
        RequestBody requestBody = RequestBody.create(json, JSON);

        // Create request
        Request request = new Request.Builder()
                .url(MANAGEECOM_API_BASE_URL+"/api/products/getProductsBySellerEmailId")
                .post(requestBody)
                .build();

        // Execute request
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
                
            }
            return response.body().string();
        }catch(Exception e) {
        	logger.error("ERROR : "+e.getMessage());
        	e.printStackTrace();
        	return "[]";
        }		
    }
}
