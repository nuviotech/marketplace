//package com.marketplace.api.util;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.marketplace.api.entitys.Seller;
//import com.marketplace.api.repository.SellerRepository;
//import org.elasticsearch.search.sort.SortOrder;
//
///**
// * @author Rohit
// * @apiNote
// * search MAP must contain those values for sort data
// *  
// * from (int) = start page (set default 0)
// * size (int) = limit of record (set default 0)
// * minPrice (float) = minPrice of product (set default 0.0f)
// * maxPrice (float) = maxPrice of product (set default 0.0f)
// * orderBy (String) = orderBy which attr (ex. sale_price) (set default "")
// * sortedOrder (SortedOrder) = 'ASC' || 'DESC' (set default "")
// * key (string) = if need (optional)
// * value (string) = if need (optional)
// * */
//
//@Service
//public class ElasticsearchUtil {
//	@Autowired
//    private RestHighLevelClient client;
//	@Autowired
//	SellerRepository sellerRepository;
//	
//	final String PRODUCTS_INDEX_NAME="products";
//	
//	Logger logger=LoggerFactory.getLogger(ElasticsearchUtil.class);
//
//	public List<String> getActiveSellerEmailIds() {
//		List<String> sellerIds=new ArrayList<>();
//		for (Seller s : sellerRepository.findByStatus("active")) {
//			sellerIds.add(s.getEmailAddr());
//		}
//		return sellerIds;
//	}
//	
//	private BoolQueryBuilder setUpInitQuery(Map<String,Object> searchMap) {
//		
//		float minPrice=(float)searchMap.get("minPrice");
//		float maxPrice=(float)searchMap.get("maxPrice");
//		
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//
//		boolQuery.must(QueryBuilders.rangeQuery("quantity").gt(0));
//        boolQuery.must(QueryBuilders.termsQuery("userId.keyword", getActiveSellerEmailIds()));
//
//        if (minPrice > 0 && maxPrice > 0) {
//            boolQuery.must(QueryBuilders.rangeQuery("sale_price").gte(minPrice).lte(maxPrice));
//        } else if (minPrice > 0) {
//            boolQuery.must(QueryBuilders.rangeQuery("sale_price").gte(minPrice));
//        } else if (maxPrice > 0) {
//            boolQuery.must(QueryBuilders.rangeQuery("sale_price").lte(maxPrice));
//        }
//        return boolQuery;
//	}
//	
//	private BoolQueryBuilder setUpInitQuery() {
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//
//		boolQuery.must(QueryBuilders.rangeQuery("quantity").gt(0));
//        boolQuery.must(QueryBuilders.termsQuery("userId.keyword", getActiveSellerEmailIds()));
//        return boolQuery;
//	}
//	
//	
//	private SearchSourceBuilder getSearchSourceBuilder(Map<String,Object> searchMap) {
//		int from=(int)searchMap.get("from");
//		int limit=(int)searchMap.get("limit");
//		String sortBy=(String)searchMap.get("sortBy");
//		String sortOrder=(String)searchMap.get("sortOrder");
//		
//		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//		
//		if(limit>0) {
//		 sourceBuilder.from(from);
//	     sourceBuilder.size(limit);
//		}
//	     // Add sorting
//	     if (sortOrder!=null && sortBy != null && !sortBy.isEmpty() && sortBy.length()>0) {
//	    	 if(sortOrder.equals("ASC"))
//	    		 sourceBuilder.sort(sortBy, SortOrder.ASC);
//	    	 else if(sortOrder.equals("DESC"))
//		         sourceBuilder.sort(sortBy, SortOrder.DESC);
//	     }
//	     return sourceBuilder;
//	}
//	
//	
//    public SearchResponse getProducts(Map<String,Object> searchMap) {
//    	try {
//    		SearchRequest searchRequest = new SearchRequest(PRODUCTS_INDEX_NAME);
//            SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(searchMap);
//
//            BoolQueryBuilder boolQuery = setUpInitQuery(searchMap);
//            sourceBuilder.query(boolQuery);
//            
//            searchRequest.source(sourceBuilder);
//            return client.search(searchRequest, RequestOptions.DEFAULT);
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    		return null;
//    	}
//    }
//    
//    
//    public SearchResponse getProductsBySimilarTitleOrKeywords(String query, Map<String,Object> searchMap){
//    	try {
//        SearchRequest searchRequest = new SearchRequest(PRODUCTS_INDEX_NAME);
//        SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(searchMap);
//
//        BoolQueryBuilder boolQuery = setUpInitQuery(searchMap);
//        boolQuery.must(QueryBuilders.multiMatchQuery(query, "title", "keywords").fuzziness("AUTO"));
//       
//        sourceBuilder.query(boolQuery);
//        searchRequest.source(sourceBuilder);
//        return client.search(searchRequest, RequestOptions.DEFAULT);
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    		return null;
//    	}
//    }
//    
//    
//    public SearchResponse getProductsByBrand(String brandName, Map<String,Object> searchMap) {
//    	try {
//	        SearchRequest searchRequest = new SearchRequest(PRODUCTS_INDEX_NAME);
//	        SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(searchMap);
//	
//	        BoolQueryBuilder boolQuery = setUpInitQuery(searchMap);
//	        boolQuery.must(QueryBuilders.matchQuery("brand", brandName));
//	        
//            sourceBuilder.query(boolQuery);
//	        searchRequest.source(sourceBuilder);
//	
//	        return client.search(searchRequest, RequestOptions.DEFAULT);
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    		return null;
//    	}
//    }
//    
//    public SearchResponse getProductsByCategory(int categoryId, Map<String,Object> searchMap) {
//    	try {
//	        SearchRequest searchRequest = new SearchRequest(PRODUCTS_INDEX_NAME);
//	        SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(searchMap);
//	
//	        BoolQueryBuilder boolQuery = setUpInitQuery(searchMap);
//	        boolQuery.must(QueryBuilders.matchQuery("categoryId", categoryId));
//	        
//            sourceBuilder.query(boolQuery);
//	        searchRequest.source(sourceBuilder);
//	
//	        return client.search(searchRequest, RequestOptions.DEFAULT);
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    		return null;
//    	}
//    }
//    
//    
//    public long getProductCount(Map<String,Object> searchMap) {
//    	try {
//	        SearchRequest searchRequest = new SearchRequest(PRODUCTS_INDEX_NAME);
//	        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//
//	        BoolQueryBuilder boolQuery = setUpInitQuery(searchMap);
//	        if(searchMap.get("key")!=null && (searchMap.get("key")+"").equals("title_keyword")) {
//	        	boolQuery.must(QueryBuilders.multiMatchQuery(searchMap.get("value"), "title", "keywords").fuzziness("AUTO"));
//	        }
//	        
//	        if(searchMap.get("key")!=null && (searchMap.get("key")+"").equals("categories")) {
//	        	boolQuery.must(QueryBuilders.matchQuery("categoryId", searchMap.get("value")));
//	        }
//	                
//	        sourceBuilder.query(boolQuery);
//	        // Set the size to 0 since we are only interested in the count
//	        sourceBuilder.size(0);
//	
//	        searchRequest.source(sourceBuilder);
//	
//	        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//	
//	        return response.getHits().getTotalHits().value;
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    		return 0;
//    	}
//    }
//
//    public GetResponse getProductById(String id) {
//    	try {
//    		GetRequest getRequest = new GetRequest(PRODUCTS_INDEX_NAME, id);
//    		return client.get(getRequest, RequestOptions.DEFAULT);
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    		return null;
//    	}
//    }
//    
//  
//    public SearchResponse getProductsByIds(List<String> productIds) {
//    	try {
//	        SearchRequest searchRequest = new SearchRequest(PRODUCTS_INDEX_NAME);
//	        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//	    	
//	        BoolQueryBuilder boolQuery = setUpInitQuery();
//	        boolQuery.must(QueryBuilders.termsQuery("productRefId", productIds));
//	
//            sourceBuilder.query(boolQuery);
//	        searchRequest.source(sourceBuilder);
//	        return client.search(searchRequest, RequestOptions.DEFAULT);
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    		return null;
//    	}
//    }
//
//    
//    
//    
//    
//}
