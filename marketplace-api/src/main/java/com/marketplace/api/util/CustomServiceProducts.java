//package com.marketplace.api.util;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.Map.Entry;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomServiceProducts implements ProductUtil {
//
//	@Autowired
//	CacheHandler cacheHandler;
//	
//	
//	public List<String> getPaginationData(int start,int end,List<String> products){
//		List<String> ptr = new ArrayList<>();
//		// pagination related logic per page 12 records
//		for (int index = start; index < products.size() && index < end; index++) {
//			ptr.add(products.get(index));
//		}
//		return ptr;
//	}
//	
//	@Override
//	public String getAllProducts(String title, Map<String, Object> map) {
//		int start=(int)map.get("from");
//		int end=start+(int)map.get("limit");		
//		
//		List<String> products = new ArrayList<>();
//		try {
//			if (title != null && title.length() > 0) {
//				for (Map.Entry<String, String> entry : cacheHandler.products.entrySet()) {
//					JSONObject jsnobject = new JSONObject(entry.getValue());
//					String productTitle = jsnobject.getString("title");
//					if (productTitle.toLowerCase().contains(title.toLowerCase())) {
//						products.add(entry.getValue());
//					}
//				}
//				return getPaginationData(start, end, products).toString();
//			} else if ((map.get("action")+"").equalsIgnoreCase("lowToHigh") || (map.get("action")+"").equalsIgnoreCase("highToLow")) {
//				List<String> result = new ArrayList<>();
//				String action=map.get("action")+"";
//				if (action.equalsIgnoreCase("lowToHigh") || action.equalsIgnoreCase("highToLow")) {
//					Map<String, Integer> unsortedMap = new HashMap<>();
//					for (Map.Entry<String, String> entry : cacheHandler.products.entrySet()) {
//						JSONObject jsnobject = new JSONObject(entry.getValue());
//						int productPrice = jsnobject.getInt("sale_price");
//						unsortedMap.put(entry.getKey(), productPrice);
//					}
//
//					boolean order;
//					if (action.equalsIgnoreCase("lowToHigh"))
//						order = true;
//					else
//						order = false;
//
//					// convert HashMap into List
//					List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortedMap.entrySet());
//					// sorting the list elements
//					Collections.sort(list, new Comparator<Entry<String, Integer>>() {
//						public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
//							if (order) {
//								// compare two object and return an integer
//								return o1.getValue().compareTo(o2.getValue());
//							} else {
//								return o2.getValue().compareTo(o1.getValue());
//							}
//						}
//					});
//					// prints the sorted HashMap
//					Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
//					for (Entry<String, Integer> entry : list) {
//						sortedMap.put(entry.getKey(), entry.getValue());
//					}
//
//					for (Entry<String, Integer> entry : sortedMap.entrySet()) {
//						result.add(cacheHandler.products.get(entry.getKey()));
//					}
//				}
//				return result.toString();
//
//			} else if ((!(map.get("minPrice")+"").equals("undefined") && !(map.get("maxPrice")+"").equals("undefined"))
//					&& Float.parseFloat((map.get("minPrice")+"")) >= 0 && Float.parseFloat((map.get("maxPrice")+"")) > 0) {
//
//				for (Map.Entry<String, String> entry : cacheHandler.products.entrySet()) {
//					JSONObject jsnobject = new JSONObject(entry.getValue());
//					int productPrice = jsnobject.getInt("sale_price");
//
//					// String keyword = jsnobject.getString("keywords");
//					if (productPrice >= Float.parseFloat((map.get("minPrice")+"")) && productPrice <= Float.parseFloat((map.get("maxPrice")+""))) {
//						products.add(entry.getValue());
//					}
//
//				}
//				List<String> resultProducts = new ArrayList<>();
//				if (products.size() > 0) {
//					for (int index = start; index < products.size() && index < end; index++) {
//						resultProducts.add(products.get(index).toString());
//					}
//				} else {
//					System.out.println(resultProducts.toString());
//				}
//				return resultProducts.toString();
//
//			} else {
//				
//
//				// pagination related logic per page 12 records
//				Map<String, String> pmap = cacheHandler.products;
//				Object values[] = pmap.values().toArray();
//				// System.out.println("page "+page);
//				for (int index = start; index < values.length && index < end; index++) {
//					products.add(values[index].toString());
//				}
//				// System.out.println(products.toString());
//				return products.toString();
//			}
//		} catch (Exception e) {
//			System.out.println("error happen : " + e.getMessage());
//			e.printStackTrace();
//			return products.toString();
//		}
//	}
//
//	@Override
//	public String searchTheRecords(String title) {
//		try {
//			List<String> products = new ArrayList<>();
//
//			for (Map.Entry<String, String> entry : cacheHandler.products.entrySet()) {
//				JSONObject jsnobject = new JSONObject(entry.getValue());
//				String productTitle = jsnobject.getString("title");
//				if (productTitle.toLowerCase().contains(title.toLowerCase())) {
//					products.add(entry.getValue());
//				}
//			}
//			return products.toString();
//		} catch (Exception e) {
//			System.out.println("Error happen (searchRecord) : " + e.getMessage());
//			e.printStackTrace();
//			return "";
//		}
//	}
//
//	@Override
//	public String getAllActiveSellerproduct() {
//		// System.out.println("get all products call");
//				List<String> products = new ArrayList<>();
//				try {
//					Map<String, String> map = cacheHandler.products;
//					Object values[] = map.keySet().toArray();
//					for (int index = 0; index < values.length; index++) {
//						JSONObject jsnobject = new JSONObject(map.get(values[index]).toString());
//
//						String title = jsnobject.getString("title");
//						title = title.replace("/", " | ").replace(" ", "-");
//						// System.out.println("title : "+title);
//						products.add("\"" + title + "&pid=" + values[index] + "\"");
//					}
//					// System.out.println("AVOID THIS (testing purpose) :::=>
//					// "+products.toString());
//					return products.toString();
//				} catch (Exception e) {
//					System.out.println("Error happen when all product fetch !!!!");
//					e.printStackTrace();
//					return products.toString();
//				}
//
//	}
//
//	@Override
//	public String getProductById(String productId) {
//		return cacheHandler.products.get(productId);
//	}
//
//	@Override
//	public String getProductBySlug(String categoryId,Map<String, Object> map) {
//		List<String> products = new ArrayList<>();
//		int start=(int)map.get("from");
//		int end=start+(int)map.get("limit");
//		// get the product by category name
//		try {
//			for (Map.Entry<String, String> entry : cacheHandler.products.entrySet()) {
//				JSONObject jsnObject = new JSONObject(entry.getValue());
//				int catId = jsnObject.getInt("categoryId");
//				if(catId==Integer.parseInt(categoryId))
//					products.add(entry.getValue());
//			}
//			return getPaginationData(start, end, products).toString();
//
//		} catch (Exception e) {
//			System.out.println("Error hapen (Categorys) : " + e.getMessage());
//			e.printStackTrace();
//			return products.toString();
//
//		}
//
//	}
//
//	@Override
//	public String getProductByBrand(String slug,Map<String, Object> map) {
//		List<String> products = new ArrayList<>();
//		try {
//			int limit=(int)map.get("limit");
//			if (limit == 0) {
//				for (Map.Entry<String, String> entry : cacheHandler.products.entrySet()) {
//					JSONObject jsnObject = new JSONObject(entry.getValue());
//					String brand = jsnObject.getString("brand");
//					if (slug.equalsIgnoreCase(brand)) {
//						products.add(entry.getValue());
//					}
//				}
//			} else {
//				Object array[] = cacheHandler.products.keySet().toArray();
//				for (int i = 0; array.length > 5 && i < 5; i++) {
//					JSONObject jsnObject = new JSONObject(cacheHandler.products.get(array[i]).toString());
//					String brand = jsnObject.getString("brand");
//					if (slug.equalsIgnoreCase(brand)) {
//						products.add(cacheHandler.products.get(array[i]));
//					}
//				}
//			}
//
//			return products.toString();
//		} catch (Exception e) {
//			System.out.println("Error (brands) : " + e.getMessage());
//			e.printStackTrace();
//			return products.toString();
//		}
//
//	}
//
//	@Override
//	public int getProductCount(Map<String, Object> map) {
//		try {
//			int count = 0;
//			String price_gt=map.get("maxPrice")+"";
//			String price_lt=map.get("minPrice")+"";
//			List<String> products = new ArrayList<>();
//
//			if((map.get("key")+"").equals("categories")) {
//				for (Map.Entry<String, String> entry : cacheHandler.products.entrySet()) {
//					JSONObject jsnObject = new JSONObject(entry.getValue());
//					int catId = jsnObject.getInt("categoryId");
//					if(catId==Integer.parseInt(map.get("value")+""))
//						products.add(entry.getValue());
//				}
//				return products.size();
//			}else if((map.get("key")+"").equals("title_keyword")) {
//
//				for (Map.Entry<String, String> entry : cacheHandler.products.entrySet()) {
//					JSONObject jsnobject = new JSONObject(entry.getValue());
//					String productTitle = jsnobject.getString("title");
//					if (productTitle.toLowerCase().contains(map.get("value")+"")) {
//						products.add(entry.getValue());
//					}
//				}
//				return products.size();
//			}else if (Float.parseFloat(price_gt) > 0.0f && Float.parseFloat(price_lt) > 0.0f) {
//				for (Map.Entry<String, String> entry : cacheHandler.products.entrySet()) {
//					JSONObject jsnobject = new JSONObject(entry.getValue());
//					int productPrice = jsnobject.getInt("sale_price");
//					// String keyword = jsnobject.getString("keywords");
//					if (productPrice >= Float.parseFloat(price_gt) && productPrice <= Float.parseFloat(price_lt)) {
//						count++;
//					}
//				}
//				
//			} else {
//				count= cacheHandler.products.size();
//			}
//			return count;
//		}catch(Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}
//
//	@Override
//	public String getProductsByIds(List<String> productIds) {
//		try {
//			List<String> ptrs = new ArrayList<>();
//			for (String id : productIds) {
//				ptrs.add(cacheHandler.products.get(id));
//			}
//			return ptrs.toString();
//		}catch(Exception e) {
//			e.printStackTrace();
//			return "[]";
//		}
//	}
//
//	@Override
//	public String getRelatedProductByKeywords(String keywords) {
//		try {
//			List<String> products = new ArrayList<>();
//			if (!keywords.equalsIgnoreCase("null") && keywords.length() > 0) {
//				for (Entry<String, String> entry : cacheHandler.products.entrySet()) {
//					JSONObject jsonObject = new JSONObject(entry.getValue());
//					Object o = jsonObject.get("keywords");
//					if (!o.equals(null)) {
//						if(jaccardSimilarity(keywords,(String)o)>=0.22) {
//							products.add(entry.getValue());
//						}
//					}
//				}
//			}
//			return products.toString();
//		} catch (Exception e) {
//			System.out.println("getRelatedProductByKeywords throws error : " + e.getMessage());
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	public double jaccardSimilarity(String s1, String s2) {
//        Set<String> set1 = nGrams(s1, 2);
//        Set<String> set2 = nGrams(s2, 2);
//        Set<String> intersection = new HashSet(set1);
//        intersection.retainAll(set2);
//        Set<String> union = new HashSet<>(set1);
//        union.addAll(set2);
//        return (double) intersection.size() / union.size();
//    }
//
//    private Set<String> nGrams(String s, int n) {
//        Set<String> nGrams = new HashSet<>();
//        for (int i = 0; i < s.length() - n + 1; i++) {
//            nGrams.add(s.substring(i, i + n));
//        }
//        return nGrams;
//    }
//
//}
