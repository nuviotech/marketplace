package com.marketplace.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.api.entitys.Seller;
import com.marketplace.api.repository.SellerRepository;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class ProductApis {	
	@Autowired
	OkHttpClient okHttpClient;

	@Autowired
	SellerRepository sellerRepository;
	
	@Value("${manageecom.api.base.url}")
	String MANAGEECOM_API_BASE_URL;
	
	Logger logger=LoggerFactory.getLogger(ProductApis.class);

	public List<Integer> getActiveSellerEmailIds() {
		List<Integer> sellerIds=new ArrayList<>();
		logger.info("### TRACE ### ENTER ProductApis.getActiveSellerEmailIds()");
		List<Seller> activeSellers = sellerRepository.findByStatus("active");
		logger.info("### TRACE ### SellerRepository.findByStatus('active') returned {} sellers", activeSellers.size());
		for (Seller s : activeSellers) {
			logger.info("### TRACE ### Active Seller -> sellerID={}, email={}, registeredSellerId={}, company={}, apiEndpoint={}", s.getSellerID(), s.getEmailAddr(), s.getRegisteredSellerId(), s.getNameOfCompany(), s.getApiEndpoint());
			//sellerIds.add(s.getEmailAddr());
			sellerIds.add(s.getRegisteredSellerId());
		}
		logger.info("### TRACE ### Active Seller IDs being sent to ManageEcom: {}", sellerIds);
		logger.info("### TRACE ### EXIT ProductApis.getActiveSellerEmailIds()");
		return sellerIds;
	}
	
// 	public String getProducts(String title,Map<String, Object> searchMap)throws IOException {
// 	    //OkHttpClient client = new OkHttpClient();
//         MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//         ObjectMapper objectMapper = new ObjectMapper();
//         Map<String,Object> dataMap = new HashMap<>();
//         dataMap.put("title", title);
//         dataMap.put("searchMap", searchMap);
//         dataMap.put("activeSellers", getActiveSellerEmailIds());
//         String json = objectMapper.writeValueAsString(dataMap);

//         // Prepare request body
//         RequestBody requestBody = RequestBody.create(json, JSON);

//         String url = MANAGEECOM_API_BASE_URL+"/api/products/getProducts";
//         logger.info("### TRACE ### [ProductApis.getProducts] Calling ManageEcom");
//         logger.info("### TRACE ### URL: {}", url);
// 		//logger.info("### TRACE ### Base URL: {}", manageEcomApiUrl);
// logger.info("### TRACE ### Endpoint: /api/products/getProducts");
// logger.info("### TRACE ### Final URL: {}", url);
//         logger.info("### TRACE ### Method: POST");
//         logger.info("### TRACE ### Payload: {}", json);

//         // Create request
//         Request request = new Request.Builder()
//                 .url(url)
//                 .post(requestBody)
//                 .build();

//         // Execute request
//         // try (Response response = okHttpClient.newCall(request).execute()) {
//         //     logger.info("### TRACE ### [ProductApis.getProducts] Status: {}", response.code());
//         //     String resBody = response.body().string();
//         //     logger.info("### TRACE ### [ProductApis.getProducts] Response: {}", resBody.length() > 300 ? resBody.substring(0, 300) + "...(truncated)" : resBody);
//         //     if (!response.isSuccessful()) {
//         //         throw new IOException("Unexpected code " + response);
//         //     }
//         //     return resBody;
//         // }catch(Exception e) {
//         // 	logger.error("### TRACE ### [ProductApis.getProducts] Error: {}", e.getMessage());
//         // 	return "[]";
//         // }	
// 		try (Response response = okHttpClient.newCall(request).execute()) {

//     logger.info("### TRACE ### [ProductApis.getProducts] Status: {}", response.code());

//     String resBody = response.body().string();

//     if (!response.isSuccessful()) {
//         throw new IOException("Unexpected code " + response);
//     }

//     /*
//      * ManageEcom may not apply the price filter correctly.
//      * Therefore, apply the price filter again in marketplace-api.
//      */
//     double minPrice = 0.0;
//     double maxPrice = Double.MAX_VALUE;

//     Object minPriceObj = searchMap.get("minPrice");
//     Object maxPriceObj = searchMap.get("maxPrice");

//     if (minPriceObj != null) {
//         minPrice = Double.parseDouble(minPriceObj.toString());
//     }

//     if (maxPriceObj != null) {
//         double parsedMaxPrice = Double.parseDouble(maxPriceObj.toString());

//         /*
//          * maxPrice = 0 means no upper limit.
//          * This is important because the default value in SellerController
//          * is 0.
//          */
//         if (parsedMaxPrice > 0) {
//             maxPrice = parsedMaxPrice;
//         }
//     }

//     logger.info(
//         "### TRACE ### Applying local price filter: {} - {}",
//         minPrice,
//         maxPrice
//     );

//     JSONArray products = new JSONArray(resBody);
//     JSONArray filteredProducts = new JSONArray();

//     for (int i = 0; i < products.length(); i++) {

//         JSONObject product = products.getJSONObject(i);

//         double salePrice = product.optDouble("sale_price", -1);

//         /*
//          * Ignore products where sale_price is missing.
//          */
//         if (salePrice < 0) {
//             continue;
//         }

//         if (salePrice >= minPrice && salePrice <= maxPrice) {
//             filteredProducts.put(product);
//         }
//     }

//     logger.info(
//         "### TRACE ### Products before price filtering: {}",
//         products.length()
//     );

//     logger.info(
//         "### TRACE ### Products after price filtering: {}",
//         filteredProducts.length()
//     );

//     return filteredProducts.toString();

// } catch (Exception e) {

//     logger.error(
//         "### TRACE ### [ProductApis.getProducts] Error: {}",
//         e.getMessage()
//     );

//     return "[]";
// }	
//     }
public String getProducts(
        String title,
        Map<String, Object> searchMap
) throws IOException {

    MediaType JSON =
            MediaType.parse(
                    "application/json; charset=utf-8"
            );

    ObjectMapper objectMapper =
            new ObjectMapper();

    /*
     * Keep the ORIGINAL pagination values.
     *
     * These are used AFTER price filtering.
     */
    int originalFrom = 0;
    int originalLimit = 20;

    if (searchMap.get("from") != null) {
        try {
            originalFrom = Integer.parseInt(
                    searchMap
                            .get("from")
                            .toString()
            );
        } catch (Exception e) {
            originalFrom = 0;
        }
    }

    if (searchMap.get("limit") != null) {
        try {
            originalLimit = Integer.parseInt(
                    searchMap
                            .get("limit")
                            .toString()
            );
        } catch (Exception e) {
            originalLimit = 20;
        }
    }

    /*
     * Extract price filter parameters early.
     *
     * Needed to determine if we should fetch all
     * products or use ManageEcom's pagination.
     */
    float minPrice = 0f;
    float maxPrice = 0f;

    if (searchMap.get("minPrice") != null) {
        try {
            minPrice = Float.parseFloat(
                    searchMap
                            .get("minPrice")
                            .toString()
            );
        } catch (Exception e) {
            minPrice = 0f;
        }
    }

    if (searchMap.get("maxPrice") != null) {
        try {
            float requestedMax = Float.parseFloat(
                    searchMap
                            .get("maxPrice")
                            .toString()
            );

            /*
             * maxPrice = 0 means no upper limit.
             * Only set maxPrice if user provided a value > 0.
             */
            if (requestedMax > 0) {
                maxPrice = requestedMax;
            }

        } catch (Exception e) {
            maxPrice = 0f;
        }
    }

    /*
     * IMPORTANT:
     *
     * Only fetch all products if we need price filtering.
     *
     * If there's no price filter, use the requested
     * pagination directly so ManageEcom can paginate correctly.
     *
     * Price filtering requires:
     * - from=0 (get all products from start)
     * - limit=10000 (large enough set for filtering)
     * - Then filter by price locally
     * - Then paginate locally
     *
     * Without price filtering:
     * - Use original from/limit for pagination at ManageEcom
     */
    Map<String, Object> manageSearchMap =
            new HashMap<>(searchMap);

    /*
     * Only treat as price filter if user explicitly
     * provided minPrice > 0 OR maxPrice > 0.
     *
     * If both are 0, there's no price filtering.
     */
    boolean hasPriceFilter =
            (minPrice > 0 || maxPrice > 0);

    logger.info(
            "### DEBUG PAGINATION ### minPrice={}, maxPrice={}, hasPriceFilter={}",
            minPrice,
            maxPrice,
            hasPriceFilter
    );

    logger.info(
            "### DEBUG PAGINATION ### Original from={}, Original limit={}",
            originalFrom,
            originalLimit
    );

    if (hasPriceFilter) {
        /*
         * Override pagination to fetch all products
         * for price filtering.
         */
        manageSearchMap.put(
                "from",
                0
        );

        /*
         * Fetch a large enough set from ManageEcom.
         *
         * This is intentionally much larger than the
         * frontend page size of 20.
         */
        manageSearchMap.put(
                "limit",
                10000
        );
    }
    /* else: Keep original from/limit for pagination */

    Map<String, Object> dataMap =
            new HashMap<>();

    dataMap.put(
            "title",
            title
    );

    dataMap.put(
            "searchMap",
            manageSearchMap
    );

    dataMap.put(
            "activeSellers",
            getActiveSellerEmailIds()
    );

    String json =
            objectMapper.writeValueAsString(
                    dataMap
            );

    /*
     * TEMPORARY DEBUG LOGGING
     *
     * Log pagination values before sending to ManageEcom.
     */
    logger.info(
            "### DEBUG PAGINATION ### ManageEcom from={}, ManageEcom limit={}",
            manageSearchMap.get("from"),
            manageSearchMap.get("limit")
    );

    logger.info(
            "### TRACE ### [ProductApis.getProducts] Calling ManageEcom"
    );

    logger.info(
            "### TRACE ### URL: {}",
            MANAGEECOM_API_BASE_URL
                    + "/api/products/getProducts"
    );

    logger.info(
            "### TRACE ### Original searchMap: {}",
            searchMap
    );

    logger.info(
            "### TRACE ### ManageEcom searchMap: {}",
            manageSearchMap
    );

    logger.info(
            "### TRACE ### Payload: {}",
            json
    );

    RequestBody requestBody =
            RequestBody.create(
                    json,
                    JSON
            );

    Request request =
            new Request.Builder()
                    .url(
                            MANAGEECOM_API_BASE_URL
                                    + "/api/products/getProducts"
                    )
                    .post(requestBody)
                    .build();

    try (
            Response response =
                    okHttpClient
                            .newCall(request)
                            .execute()
    ) {

        logger.info(
                "### TRACE ### [ProductApis.getProducts] Status: {}",
                response.code()
        );

        String resBody =
                response.body().string();

        logger.info(
                "### TRACE ### ManageEcom response length: {}",
                resBody.length()
        );

        if (!response.isSuccessful()) {
            throw new IOException(
                    "Unexpected code " + response
            );
        }

        /*
         * Convert JSON into product list.
         */
        List<Map<String, Object>> products =
                objectMapper.readValue(
                        resBody,
                        new TypeReference<
                                List<Map<String, Object>>
                        >() {}
                );

        /*
         * minPrice and maxPrice were already extracted
         * at the beginning of this method.
         *
         * Log the values for debugging.
         */
        logger.info(
                "### PRICE FILTER ### minPrice={}, maxPrice={}",
                minPrice,
                maxPrice
        );

        /*
         * Filter products using sale_price.
         *
         * Only apply price filtering if hasPriceFilter is true.
         * Otherwise, use all products from ManageEcom.
         */
        List<Map<String, Object>>
                filteredProducts =
                new ArrayList<>();

        if (hasPriceFilter) {
            /*
             * Apply price range filter.
             */
            for (
                    Map<String, Object> product
                    : products
            ) {

                Object salePriceObject =
                        product.get("sale_price");

                if (salePriceObject == null) {
                    continue;
                }

                float salePrice;

                try {

                    salePrice =
                            Float.parseFloat(
                                    salePriceObject
                                            .toString()
                            );

                } catch (Exception e) {
                    continue;
                }

                /*
                 * Inclusive range:
                 *
                 * 200 <= sale_price <= 500
                 */
                if (
                        salePrice >= minPrice &&
                        salePrice <= maxPrice
                ) {

                    filteredProducts.add(
                            product
                    );
                }
            }
        } else {
            /*
             * No price filter: use all products from ManageEcom.
             */
            filteredProducts.addAll(products);
        }

        /*
         * RE-APPLY SORTING after price filtering.
         *
         * Price filtering destroys the sort order,
         * so we must re-sort before pagination.
         */
        String sortOrder = null;
        String sortBy = null;

        if (searchMap.get("sortOrder") != null) {
            sortOrder = searchMap.get("sortOrder").toString();
        }

        if (searchMap.get("sortBy") != null) {
            sortBy = searchMap.get("sortBy").toString();
        }

        if (
                sortOrder != null &&
                !sortOrder.equalsIgnoreCase("null") &&
                sortBy != null
        ) {

            final String finalSortOrder = sortOrder;

            if (
                    sortBy.equalsIgnoreCase("sale_price")
            ) {
                filteredProducts.sort(
                        (p1, p2) -> {

                            Object price1Object =
                                    p1.get("sale_price");
                            Object price2Object =
                                    p2.get("sale_price");

                            float price1 = 0f;
                            float price2 = 0f;

                            try {

                                if (
                                        price1Object
                                                != null
                                ) {
                                    price1 =
                                            Float.parseFloat(
                                                    price1Object
                                                            .toString()
                                            );
                                }

                                if (
                                        price2Object
                                                != null
                                ) {
                                    price2 =
                                            Float.parseFloat(
                                                    price2Object
                                                            .toString()
                                            );
                                }

                            } catch (Exception e) {
                                logger.warn(
                                        "### SORT ERROR ### Failed to parse prices",
                                        e
                                );
                            }

                            if (
                                    finalSortOrder
                                            .equalsIgnoreCase(
                                                    "ASC"
                                            )
                            ) {
                                return Float.compare(
                                        price1,
                                        price2
                                );
                            } else {
                                return Float.compare(
                                        price2,
                                        price1
                                );
                            }
                        }
                );

                logger.info(
                        "### SORTING ### Sorted {} products by {} ({})",
                        filteredProducts.size(),
                        sortBy,
                        sortOrder
                );

            } else if (
                    sortBy.equalsIgnoreCase(
                            "avgRating"
                    )
            ) {

                filteredProducts.sort(
                        (p1, p2) -> {

                            Object rating1Object =
                                    p1.get("avgRating");
                            Object rating2Object =
                                    p2.get("avgRating");

                            float rating1 = 0f;
                            float rating2 = 0f;

                            try {

                                if (
                                        rating1Object
                                                != null
                                ) {
                                    rating1 =
                                            Float.parseFloat(
                                                    rating1Object
                                                            .toString()
                                            );
                                }

                                if (
                                        rating2Object
                                                != null
                                ) {
                                    rating2 =
                                            Float.parseFloat(
                                                    rating2Object
                                                            .toString()
                                            );
                                }

                            } catch (Exception e) {
                                logger.warn(
                                        "### SORT ERROR ### Failed to parse ratings",
                                        e
                                );
                            }

                            if (
                                    finalSortOrder
                                            .equalsIgnoreCase(
                                                    "ASC"
                                            )
                            ) {
                                return Float.compare(
                                        rating1,
                                        rating2
                                );
                            } else {
                                return Float.compare(
                                        rating2,
                                        rating1
                                );
                            }
                        }
                );

                logger.info(
                        "### SORTING ### Sorted {} products by {} ({})",
                        filteredProducts.size(),
                        sortBy,
                        sortOrder
                );
            }
        }

        /*
         * Apply pagination.
         *
         * If there was a price filter:
         *   - We fetched from=0, limit=10000 from ManageEcom
         *   - We need to slice locally for the requested page
         *
         * If there was NO price filter:
         *   - ManageEcom already paginated (from=originalFrom, limit=originalLimit)
         *   - Use the result as-is, no local slicing needed
         */
        List<Map<String, Object>>
                paginatedProducts;

        if (hasPriceFilter) {
            /*
             * We fetched all products, apply local pagination.
             */
            int startIndex =
                    Math.min(
                            originalFrom,
                            filteredProducts.size()
                    );

            int endIndex =
                    Math.min(
                            startIndex
                                    + originalLimit,
                            filteredProducts.size()
                    );

            paginatedProducts =
                    filteredProducts.subList(
                            startIndex,
                            endIndex
                    );
        } else {
            /*
             * ManageEcom already paginated the results.
             * Use as-is without local slicing.
             */
            paginatedProducts =
                    filteredProducts;
        }

        logger.info(
                "### PRICE FILTER ### ManageEcom returned: {}",
                products.size()
        );

        logger.info(
                "### PRICE FILTER ### After filtering: {}",
                filteredProducts.size()
        );

        logger.info(
                "### PRICE FILTER ### Original from: {}",
                originalFrom
        );

        logger.info(
                "### PRICE FILTER ### Original limit: {}",
                originalLimit
        );

        logger.info(
                "### PRICE FILTER ### Returning: {}",
                paginatedProducts.size()
        );

        return objectMapper.writeValueAsString(
                paginatedProducts
        );

    } catch (Exception e) {

        logger.error(
                "### TRACE ### [ProductApis.getProducts] Error: {}",
                e.getMessage(),
                e
        );

        return "[]";
    }
}
	public String getProductsBySimilarTitleOrKeywords(String query, Map<String,Object> searchMap) {
		try {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("title", query);
        dataMap.put("searchMap", searchMap);
        dataMap.put("activeSellers", getActiveSellerEmailIds());
        String json = objectMapper.writeValueAsString(dataMap);

        // Prepare request body
        RequestBody requestBody = RequestBody.create(json, JSON);

        // Create request
        Request request = new Request.Builder()
                .url(MANAGEECOM_API_BASE_URL+"/api/products/searchTheRecords")
                .post(requestBody)
                .build();

        // Execute request
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
                
            }
            return response.body().string();
        }
        
		}catch(Exception e) {
			e.printStackTrace();
			return"[]";
		}
	}
	
	public String getActiveSellerProducts() {
		try {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("activeSellers", getActiveSellerEmailIds());
        String json = objectMapper.writeValueAsString(dataMap);

        // Prepare request body
        RequestBody requestBody = RequestBody.create(json, JSON);
        // Create request
        Request request = new Request.Builder()
                .url(MANAGEECOM_API_BASE_URL+"/api/products/getAllActiveSellerProducts")
                .post(requestBody)
                .build();

        // Execute request
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
                
            }
            return response.body().string();
        }
        
		}catch(Exception e) {
			e.printStackTrace();
			return"[]";
		}
	}
	
	public String getProductsByBrand(String brandName, Map<String,Object> searchMap) {
		try {
			if(brandName.equals("undefined"))
				return "[]";
	        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	        ObjectMapper objectMapper = new ObjectMapper();
	        Map<String,Object> dataMap = new HashMap<>();
	        dataMap.put("brandName", brandName);
	        dataMap.put("searchMap", searchMap);
	        dataMap.put("activeSellers", getActiveSellerEmailIds());
	        String json = objectMapper.writeValueAsString(dataMap);

	        // Prepare request body
	        RequestBody requestBody = RequestBody.create(json, JSON);
	        // Create request
	        Request request = new Request.Builder()
	                .url(MANAGEECOM_API_BASE_URL+"/api/products/getByBrand")
	                .post(requestBody)
	                .build();

	        // Execute request
	        try (Response response = okHttpClient.newCall(request).execute()) {
	            if (!response.isSuccessful()) {
	                throw new IOException("Unexpected code " + response);
	                
	            }
	            return response.body().string();
	        }
	        
		}catch(Exception e) {
			e.printStackTrace();
			return"[]";
		}
	}
	
	public String getProductsByCategory(String categoryId, Map<String,Object> searchMap) {
		try {
	        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	        ObjectMapper objectMapper = new ObjectMapper();
	        Map<String,Object> dataMap = new HashMap<>();
	        dataMap.put("categoryId", categoryId);
	        dataMap.put("searchMap", searchMap);
	        dataMap.put("activeSellers", getActiveSellerEmailIds());
	        String json = objectMapper.writeValueAsString(dataMap);

	        // Prepare request body
	        RequestBody requestBody = RequestBody.create(json, JSON);
	        // Create request
	        Request request = new Request.Builder()
	                .url(MANAGEECOM_API_BASE_URL+"/api/products/getBycategory")
	                .post(requestBody)
	                .build();

	        // Execute request
	        try (Response response = okHttpClient.newCall(request).execute()) {
	            if (!response.isSuccessful()) {
	                throw new IOException("Unexpected code " + response);
	                
	            }
	            return response.body().string();
	        }
	        
		}catch(Exception e) {
			e.printStackTrace();
			return"[]";
		}
	}
	
	public String getProductCount(
        Map<String, Object> searchMap
) {

    try {

        float minPrice = 0f;
        float maxPrice = 0f;

        // ---------------------------------------------
        // Read price filters
        // ---------------------------------------------

        if (searchMap.get("minPrice") != null) {
            try {
                minPrice = Float.parseFloat(
                        searchMap.get("minPrice").toString()
                );
            } catch (Exception e) {
                minPrice = 0f;
            }
        }

        if (searchMap.get("maxPrice") != null) {
            try {
                maxPrice = Float.parseFloat(
                        searchMap.get("maxPrice").toString()
                );
            } catch (Exception e) {
                maxPrice = 0f;
            }
        }

        // ---------------------------------------------
        // Read title
        // ---------------------------------------------

        String title = "";

        if (searchMap.get("title") != null) {
            title = searchMap.get("title").toString().trim();
        }

        logger.info(
                "### PRODUCT COUNT ### title={}, minPrice={}, maxPrice={}",
                title,
                minPrice,
                maxPrice
        );

        // =========================================================
// TITLE SEARCH
// =========================================================

if (!title.isEmpty()) {

    logger.info(
            "### PRODUCT COUNT ### TITLE SEARCH: {}",
            title
    );

    Map<String, Object> titleSearchMap =
            new HashMap<>();

    titleSearchMap.put(
            "minPrice",
            minPrice
    );

    titleSearchMap.put(
            "maxPrice",
            maxPrice
    );

    titleSearchMap.put(
            "sortOrder",
            ""
    );

    titleSearchMap.put(
            "sortBy",
            ""
    );

    // IMPORTANT:
    // Fetch all matching products for count
    titleSearchMap.put(
            "from",
            0
    );

    titleSearchMap.put(
            "limit",
            10000
    );

    String result =
            getProducts(
                    title,
                    titleSearchMap
            );

    if (
            result == null ||
            result.trim().isEmpty()
    ) {
        return "0";
    }

    ObjectMapper objectMapper =
            new ObjectMapper();

    List<Map<String, Object>> products =
            objectMapper.readValue(
                    result,
                    new TypeReference<
                            List<Map<String, Object>>
                    >() {}
            );

    logger.info(
            "### PRODUCT COUNT ### TITLE SEARCH RESULTS: {}",
            products.size()
    );

    return String.valueOf(
            products.size()
    );
}
        // =========================================================
        // NO TITLE SEARCH
        // =========================================================

        /*
         * If there is no title search and there is
         * no price filter, use ManageEcom count API.
         */

        if (
                minPrice <= 0 &&
                maxPrice <= 0
        ) {

            Map<String, Object> dataMap =
                    new HashMap<>();

            dataMap.put(
                    "searchMap",
                    searchMap
            );

            dataMap.put(
                    "activeSellers",
                    getActiveSellerEmailIds()
            );

            ObjectMapper objectMapper =
                    new ObjectMapper();

            String json =
                    objectMapper.writeValueAsString(
                            dataMap
                    );

            MediaType JSON =
                    MediaType.parse(
                            "application/json; charset=utf-8"
                    );

            RequestBody requestBody =
                    RequestBody.create(
                            json,
                            JSON
                    );

            Request request =
                    new Request.Builder()
                            .url(
                                    MANAGEECOM_API_BASE_URL
                                            + "/api/products/getCount"
                            )
                            .post(requestBody)
                            .build();

            try (
                    Response response =
                            okHttpClient
                                    .newCall(request)
                                    .execute()
            ) {

                if (!response.isSuccessful()) {
                    throw new IOException(
                            "Unexpected code " + response
                    );
                }

                return response
                        .body()
                        .string();
            }
        }

        // =========================================================
        // PRICE FILTER ONLY
        // =========================================================

        ObjectMapper objectMapper =
                new ObjectMapper();

        Map<String, Object> manageSearchMap =
                new HashMap<>();

        if (searchMap.get("key") != null) {
            manageSearchMap.put(
                    "key",
                    searchMap.get("key")
            );
        }

        if (searchMap.get("value") != null) {
            manageSearchMap.put(
                    "value",
                    searchMap.get("value")
            );
        }

        manageSearchMap.put(
                "minPrice",
                0f
        );

        manageSearchMap.put(
                "maxPrice",
                0f
        );

        manageSearchMap.put(
                "from",
                0
        );

        manageSearchMap.put(
                "limit",
                10000
        );

        Map<String, Object> dataMap =
                new HashMap<>();

        dataMap.put(
                "title",
                null
        );

        dataMap.put(
                "searchMap",
                manageSearchMap
        );

        dataMap.put(
                "activeSellers",
                getActiveSellerEmailIds()
        );

        String json =
                objectMapper.writeValueAsString(
                        dataMap
                );

        MediaType JSON =
                MediaType.parse(
                        "application/json; charset=utf-8"
                );

        RequestBody requestBody =
                RequestBody.create(
                        json,
                        JSON
                );

        Request request =
                new Request.Builder()
                        .url(
                                MANAGEECOM_API_BASE_URL
                                        + "/api/products/getProducts"
                        )
                        .post(requestBody)
                        .build();

        try (
                Response response =
                        okHttpClient
                                .newCall(request)
                                .execute()
        ) {

            if (!response.isSuccessful()) {
                throw new IOException(
                        "Unexpected code " + response
                );
            }

            String resBody =
                    response.body().string();

            List<Map<String, Object>> products =
                    objectMapper.readValue(
                            resBody,
                            new TypeReference<
                                    List<Map<String, Object>
                            >>() {}
                    );

            int count = 0;

            for (
                    Map<String, Object> product
                    : products
            ) {

                Object salePriceObject =
                        product.get("sale_price");

                if (salePriceObject == null) {
                    continue;
                }

                float salePrice;

                try {
                    salePrice =
                            Float.parseFloat(
                                    salePriceObject
                                            .toString()
                            );
                } catch (Exception e) {
                    continue;
                }

                if (
                        salePrice >= minPrice &&
                        salePrice <= maxPrice
                ) {
                    count++;
                }
            }

            logger.info(
                    "### PRODUCT COUNT ### ManageEcom returned: {}",
                    products.size()
            );

            logger.info(
                    "### PRODUCT COUNT ### Filtered count: {}",
                    count
            );

            return String.valueOf(count);
        }

    } catch (Exception e) {

        logger.error(
                "### PRODUCT COUNT ERROR ### {}",
                e.getMessage(),
                e
        );

        return "0";
    }
}

	public String getProductById(String id) {
		try {
	        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	        ObjectMapper objectMapper = new ObjectMapper();
	        Map<String,Object> dataMap = new HashMap<>();
	        dataMap.put("id", id);
	        dataMap.put("activeSellers", getActiveSellerEmailIds());
	        String json = objectMapper.writeValueAsString(dataMap);

	        // Prepare request body
	        RequestBody requestBody = RequestBody.create(json, JSON);

	        // Create request
	        Request request = new Request.Builder()
	                .url(MANAGEECOM_API_BASE_URL+"/api/products/byId")
	                .post(requestBody)
	                .build();

	        // Execute request
	        try (Response response = okHttpClient.newCall(request).execute()) {
	            if (!response.isSuccessful()) {
	                throw new IOException("Unexpected code " + response);
	                
	            }
	            return response.body().string();
	        }
	        
			}catch(Exception e) {
				e.printStackTrace();
				return"{}";
			}
	}
	
	public String getProductsByIds(List<String> productIds) {
		try {
	        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	        ObjectMapper objectMapper = new ObjectMapper();
	        Map<String,Object> dataMap = new HashMap<>();
	        dataMap.put("productIds", productIds);
	        dataMap.put("activeSellers", getActiveSellerEmailIds());
	        String json = objectMapper.writeValueAsString(dataMap);

	        // Prepare request body
	        RequestBody requestBody = RequestBody.create(json, JSON);
	        // Create request
	        Request request = new Request.Builder()
	                .url(MANAGEECOM_API_BASE_URL+"/api/products/getByIds")
	                .post(requestBody)
	                .build();

	        // Execute request
	        try (Response response = okHttpClient.newCall(request).execute()) {
	            if (!response.isSuccessful()) {
	                throw new IOException("Unexpected code " + response);
	                
	            }
	            return response.body().string();
	        }
	        
		}catch(Exception e) {
			e.printStackTrace();
			return"[]";
		}
	}
	
	
	
	public String getProductVariations(String variationId) {
		try {
	        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	        ObjectMapper objectMapper = new ObjectMapper();
	        Map<String,Object> dataMap = new HashMap<>();
	        dataMap.put("variationId", variationId);
	        dataMap.put("activeSellers", getActiveSellerEmailIds());
	        String json = objectMapper.writeValueAsString(dataMap);

	        // Prepare request body
	        RequestBody requestBody = RequestBody.create(json, JSON);
	        // Create request
	        Request request = new Request.Builder()
	                .url(MANAGEECOM_API_BASE_URL+"/api/products/getProductVariations")
	                .post(requestBody)
	                .build();

	        // Execute request
	        try (Response response = okHttpClient.newCall(request).execute()) {
	            if (!response.isSuccessful()) {
	                throw new IOException("Unexpected code " + response);
	                
	            }
	            return response.body().string();
	        }
	        
		}catch(Exception e) {
			e.printStackTrace();
			return"[]";
		}
	}
	
	
}
