//package com.marketplace.api.util;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.elasticsearch.search.SearchHit;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Service
//public class ElasticsearchServiceProducts implements ProductUtil {
//
//	@Autowired
//	ElasticsearchUtil elasticsearchUtil;
//	
//	@Override
//	public String getAllProducts(String title,Map<String, Object> searchMap) {
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//            List<Map<String, Object>> products = new ArrayList<>();
//            
//			if (title != null && title.length() > 0) {
//				for (SearchHit hit : elasticsearchUtil.getProductsBySimilarTitleOrKeywords(title,searchMap).getHits().getHits()) {
//		               products.add(hit.getSourceAsMap());
//		         }
//	            return objectMapper.writeValueAsString(products);
//			} else {
//				for (SearchHit hit : elasticsearchUtil.getProducts(searchMap).getHits().getHits()) {
//		               products.add(hit.getSourceAsMap());
//		         }
//	            return objectMapper.writeValueAsString(products);
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//			return "[]";
//		}
//	}
//	
//
//	@Override
//	public String searchTheRecords(String title) {
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//            List<Map<String, Object>> products = new ArrayList<>();
//			for (SearchHit hit : elasticsearchUtil.getProductsBySimilarTitleOrKeywords(title,Map.of("minPrice",0.0f,"maxPrice",0.0f,"sortOrder","","limit",10,"from",0,"sortBy","")).getHits().getHits()) {
//	               products.add(hit.getSourceAsMap());
//	         }
//         return objectMapper.writeValueAsString(products);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//	}
//
//	@Override
//	public String getAllActiveSellerproduct() {
//		try {
//			List<String> products = new ArrayList<>();
//			for (SearchHit hit : elasticsearchUtil.getProducts(Map.of("minPrice",0.0f,"maxPrice",0.0f,"sortOrder","","limit",100,"from",0,"sortBy","")).getHits().getHits()) {
//				JSONObject jsnobject = new JSONObject(hit.getSourceAsMap());
//				String title = jsnobject.getString("title");
//				title = title.replace("/", " | ").replace(" ", "-");
//				products.add("\"" + title + "&pid=" + jsnobject.getString("productRefId")  + "\"");
//	         }
//			return products.toString();
//		}catch(Exception e) {
//			e.printStackTrace();
//			return "[]";
//		}
//	}
//
//	@Override
//	public String getProductById(String productId) {
//		try {
//			return elasticsearchUtil.getProductById(productId).getSourceAsString();
//		}catch(Exception e) {
//			e.printStackTrace();
//			return "{}";
//		}
//	}
//
//	@Override
//	public String getProductBySlug(String categoryId,Map<String, Object> searchMap) {
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//            List<Map<String, Object>> products = new ArrayList<>();
//			if (Integer.parseInt(categoryId) > 0) {
//				for (SearchHit hit : elasticsearchUtil.getProductsByCategory(Integer.parseInt(categoryId),searchMap).getHits().getHits()) {
//		               products.add(hit.getSourceAsMap());
//		         }
//			}
//			
//	        return objectMapper.writeValueAsString(products);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return "[]";
//		}
//	}
//
//	@Override
//	public String getProductByBrand(String brand,Map<String, Object> searchMap) {
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//            List<Map<String, Object>> products = new ArrayList<>();
//			if (brand!=null && brand.length()> 0) {
//				for (SearchHit hit : elasticsearchUtil.getProductsByBrand(brand,searchMap).getHits().getHits()) {
//		               products.add(hit.getSourceAsMap());
//		         }
//			}
//	        return objectMapper.writeValueAsString(products);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return "[]";
//		}
//	}
//
//	@Override
//	public int getProductCount(Map<String, Object> searchMap) {
//		try {
//			return (int)elasticsearchUtil.getProductCount(searchMap);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}
//
//	@Override
//	public String getProductsByIds(List<String> ids) {
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//            List<Map<String, Object>> products = new ArrayList<>();
//            for (SearchHit hit : elasticsearchUtil.getProductsByIds(ids).getHits().getHits()) {
//	               products.add(hit.getSourceAsMap());
//	         }
//	        return objectMapper.writeValueAsString(products);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return "[]";
//		}
//	}
//
//	@Override
//	public String getRelatedProductByKeywords(String keywords) {
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//            List<Map<String, Object>> products = new ArrayList<>();
//			for (SearchHit hit : elasticsearchUtil.getProductsBySimilarTitleOrKeywords(keywords,Map.of("minPrice",0.0f,"maxPrice",0.0f,"sortOrder","","limit",20,"from",0,"sortBy","")).getHits().getHits()) {
//	               products.add(hit.getSourceAsMap());
//	         }
//         return objectMapper.writeValueAsString(products);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return "[]";
//		}
//	}
//
//}
