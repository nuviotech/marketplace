//package marketplace.admin.api.marketplace_Admin.util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ElasticsearchUtil {
//	@Autowired
//    private RestHighLevelClient client;
//	
//	final String PRODUCTS_INDEX_NAME="products";
//	Logger logger=LoggerFactory.getLogger(ElasticsearchUtil.class);
//	
//	public long getProductCountBySellerId(String sellerId){
//		try {
//	        SearchRequest searchRequest = new SearchRequest(PRODUCTS_INDEX_NAME);
//	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//	        searchSourceBuilder.query(QueryBuilders.matchQuery("userId.keyword", sellerId));
//	        searchSourceBuilder.size(0); // No documents in response, only count
//	        searchRequest.source(searchSourceBuilder);
//	        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//	        return searchResponse.getHits().getTotalHits().value;
//		}catch(Exception e) {
//			logger.error("ERROR : "+e.getMessage());
//			e.printStackTrace();
//			return -1;
//		}
//    }
//	
//	public List<Products> getProductsBySellerId(String sellerId, int pageNo, int pageSize) throws Exception {
//        int from = (pageNo - 1) * pageSize; // Calculate the starting index for pagination
//        
//        SearchRequest searchRequest = new SearchRequest("your_index_name");
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        
//        // Match documents where the "userId" field equals the specified sellerId
//        sourceBuilder.query(QueryBuilders.matchQuery("userId", sellerId));
//        
//        // Set the size to retrieve pageSize documents
//        sourceBuilder.size(pageSize);
//        
//        // Set the starting index for pagination
//        sourceBuilder.from(from);
//        
//        // Assign the sourceBuilder to the searchRequest
//        searchRequest.source(sourceBuilder);
//        
//        // Execute the search request and retrieve the response
//        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        
//        // Process the search hits and extract product details
//        List<Products> products = new ArrayList<>(pageSize);
//        for (SearchHit hit : searchResponse.getHits().getHits()) {
//           
//            // Extract other fields as needed
//            JSONArray array = new JSONArray(hit.getSourceAsMap().get("images").toString());
//            String mainImgUrl =new JSONObject(array.get(1)+"").getString("url");
//            
//            // Create a Product object and add it to the list
//            Products product = new Products(
//            		hit.getSourceAsMap().get("productRefId").toString(),
//            		hit.getSourceAsMap().get("brand").toString(),
//            		hit.getSourceAsMap().get("color").toString(),
//            		hit.getSourceAsMap().get("createdDtTime").toString(),
//            		hit.getSourceAsMap().get("sku").toString(),
//            		hit.getSourceAsMap().get("description").toString(),
//            		hit.getSourceAsMap().get("material").toString(),
//            		(float)hit.getSourceAsMap().get("price"),
//            		(float)hit.getSourceAsMap().get("sale_price"),
//            		hit.getSourceAsMap().get("title").toString(),
//            		null /*vendor details*/,
//            		mainImgUrl,/*main image*/
//            		hit.getSourceAsMap().get("images").toString(),
//            		hit.getSourceAsMap().get("userId").toString(),
//            		(int)hit.getSourceAsMap().get("categoryId"),
//            		null /*slug other info*/,
//            		hit.getSourceAsMap().get("totalRatingRation").toString(),
//            		(int)hit.getSourceAsMap().get("quantity"),
//            		hit.getSourceAsMap().get("companyName").toString()
//            		);
//            products.add(product);
//        }
//        // Return the list of products
//        return products;
//    }
//
//		
//
//	
//}
