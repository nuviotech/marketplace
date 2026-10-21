package marketplace.admin.api.marketplace_Admin.Implementation;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.enititys.Seller;
import marketplace.admin.api.marketplace_Admin.repository.MarketplaceAdminRepository;
import marketplace.admin.api.marketplace_Admin.repository.SellerRepository;
import marketplace.admin.api.marketplace_Admin.service.ApiService;
import marketplace.admin.api.marketplace_Admin.service.SellerService;
import marketplace.admin.api.marketplace_Admin.util.ProductApis;
import marketplace.admin.api.marketplace_Admin.util.Products;

@Service
public class SellerServiceImpl implements SellerService {
	@Autowired
	SellerRepository activeSellerRepository;
	@Autowired
	MarketplaceAdminRepository marketplaceAdminRepository;
	@Autowired
	ProductApis productApis;
	
	@Autowired
	ApiService apiService;
	
	@Value("${marketplace.api.url}")
	String marketplaceApiUrl;
	
	@Value("${manageEcom.api.url}")
	String manageEcomApiUrl;
	
	@Value("${apiKey}")
	String apiKey;
	
	@Value("${policyMaker.url}")
	private String baseUrl;
	
	Logger logger = LoggerFactory.getLogger(SellerServiceImpl.class);
	
	@Override
	public String getAllSellers(int page,String searchKey,String searchValue, Model model) {
		int totalRecord=0;
		List<Seller> sellers = new ArrayList<>();
		try {
			//get totalnumber of record
			var url1=baseUrl+"/getTotalCountOfSeller?searchKey="+searchKey+"&searchValue="+URLEncoder.encode(searchValue, StandardCharsets.UTF_8);;
			var request1=HttpRequest.newBuilder().GET().uri(URI.create(url1)).setHeader("apikey",apiKey).build();
			var client1=HttpClient.newBuilder().build();
			var res1=client1.send(request1,HttpResponse.BodyHandlers.ofString());
			totalRecord = Integer.parseInt(res1.body().toString());
			
			var url=baseUrl+"/getAllSeller?page="+page+"&numOfrecord=7&searchKey="+searchKey+"&searchValue="+URLEncoder.encode(searchValue, StandardCharsets.UTF_8);;
			var request=HttpRequest.newBuilder().POST(BodyPublishers.ofString("")).uri(URI.create(url)).setHeader("apikey",apiKey).build();
			var client=HttpClient.newBuilder().build();
			var res=client.send(request,HttpResponse.BodyHandlers.ofString());
			JSONArray main = new JSONArray(res.body());
			
			JSONArray jsonArray = new JSONArray(main.get(0)+""); 
			if (jsonArray != null) {  
	            //Iterating JSON array  
	            for (int i=0;i<jsonArray.length();i++){   
	            	JSONObject jsnobject = new JSONObject(jsonArray.get(i).toString());
	            	String lastname = jsnobject.getString("lastName");
	            	Seller seller=new Seller();
	            	seller.setFirstName(jsnobject.getString("firstName"));
	            	seller.setLastName(jsnobject.getString("lastName"));
	            	seller.setEmailAddr(jsnobject.getString("emailAddr"));
	            	seller.setPhoneNumber(jsnobject.getString("phoneNumber"));
	            	seller.setNameOfCompany(jsnobject.get("nameOfCompany")+"");
	            	seller.setComponyUrl(jsnobject.get("componyUrl")+"");
	            	seller.setBussinessAddr(jsnobject.get("bussinessAddr")+"");
	            	JSONArray catArray = new JSONArray(jsnobject.get("categorysList")+"");
	            	String categories="";
	            	for (int j=0;j<catArray.length();j++){   
		            	JSONObject jsnCat= new JSONObject(catArray.get(j).toString());
		            	categories+=jsnCat.getString("name")+",";
	            	}
	            	seller.setCategories(categories);
	            	seller.setProductTotalCount((int)productApis.getProductCountBySellerId(seller.getEmailAddr()));
	            	sellers.add(seller);
	            } 
	            model.addAttribute("sellers", sellers);   
	            model.addAttribute("categories",main.get(1));
	        }  
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",400);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
		//model.addAttribute("user",new User());
		if(searchKey.length()==0) {
			model.addAttribute("totalPage",Math.ceil(totalRecord/7)+1);
			model.addAttribute("currentPage", page);
		}else {
			model.addAttribute("totalPage",1);
			model.addAttribute("currentPage",0);
		}
        model.addAttribute("status", "all");
		//model.addAttribute("sellers", sellers);
		return "UserPages/sellerTable";
	}

	@Override
	public String getActiveSeller(Model model, String status, int page, int limit) {
		try {
			Pageable pageable=PageRequest.of(page, limit, Sort.by("createdDtTime").descending());
			Page<Seller> sellersPage= activeSellerRepository.findByStatus(status,pageable);
			
			List<Seller> sellers = new ArrayList<>(limit);
			for(Seller  seller : sellersPage) {
				seller.setProductTotalCount((int)productApis.getProductCountBySellerId(seller.getEmailAddr()));
				sellers.add(seller);
			}
			
			model.addAttribute("status", status);
			model.addAttribute("sellers", sellers);
			//model.addAttribute("user",user);
			model.addAttribute("totalPage",sellersPage.getTotalPages());
	        model.addAttribute("currentPage", page);
	        return "UserPages/sellerTable";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";		
		}
	}

	@Override
	public String activeTheSeller(String id, Model model, Principal p, MarketplaceAdmin user) {
		try {
			Seller s=activeSellerRepository.findByEmailAddrAndStatus(id,"active");
			if(s==null) {
				var url=baseUrl+"/getSellerById?id="+id;
				var request=HttpRequest.newBuilder().GET().uri(URI.create(url)).setHeader("apikey",apiKey).build();
				var client=HttpClient.newBuilder().build();
				var res=client.send(request,HttpResponse.BodyHandlers.ofString());
				JSONObject jsnobject=new JSONObject(res.body().toString());
				Seller seller=null;
				seller=activeSellerRepository.findByEmailAddr(id);
				if(seller==null) {
					seller=new Seller();
		        	seller.setFirstName(jsnobject.getString("firstName"));
		        	seller.setLastName(jsnobject.getString("lastName"));
		        	seller.setEmailAddr(jsnobject.getString("emailAddr"));
		        	seller.setPhoneNumber(jsnobject.getString("phoneNumber"));
		        	seller.setNameOfCompany(jsnobject.getString("nameOfCompany"));
		        	seller.setComponyUrl(jsnobject.get("componyUrl")+"");
		        	seller.setBussinessAddr(jsnobject.get("bussinessAddr")+"");
		        	seller.setPincode(jsnobject.get("pincode")+"");
		        	seller.setCountry(jsnobject.get("country")+"");
		        	seller.setStatus("pending");
		        	seller.setApiEndpoint(manageEcomApiUrl);
		        	seller.setGstNo(jsnobject.get("gstNo")+"");
		        	seller.setState(jsnobject.get("state")+"");
		        	seller.setMarketplaceId(user.getEmail());
		        	String categories="";
	            	JSONArray catArray = new JSONArray(jsnobject.get("categorysList")+"");
	            	for (int j=0;j<catArray.length();j++){   
		            	JSONObject jsnCat= new JSONObject(catArray.get(j).toString());
		            	categories+=jsnCat.getString("name")+",";
	            	}
	            	seller.setCategories(categories);
				}else {
		        	seller.setStatus("pending");
		        	seller.setApiEndpoint(manageEcomApiUrl);
		        	seller.setMarketplaceId(user.getEmail());
				}
				
	        	Map<String,String> map=new HashMap();
//	        	ObjectMapper objectMapper = new ObjectMapper();
	        	user = marketplaceAdminRepository.findByEmail(p.getName());
	        	map.put("marketplaceApiUrl", marketplaceApiUrl);
	        	map.put("sellerEmail", jsnobject.getString("emailAddr"));
	        	map.put("marketplaceName",user.getCompanyName());
	        	map.put("marketplaceEmail",user.getEmail());
	        	map.put("marketplaceContact",user.getContact());
	        	map.put("firstName", user.getName());
	        	
	        	String verifyRes=apiService.verifyMarketplace(map);
	        	if(verifyRes.equals("2")) {
	        		//status 2 means marketplace total user limit exceeded...
	        		System.err.println("ERROR : marketplace total user limit exceeded...");
	        		model.addAttribute("errCode","⚠️");
	    			model.addAttribute("errMsg","Sorry,marketplace total user limit exceeded...");
	    			return "UserPages/error";
	        	}
	        	//seller limit set here (5 seller each marketplace)
	        	if(activeSellerRepository.countByStatus("active")==5) {
	        		System.err.println("ERROR : marketplace active seller limit exceeded...");
	        		model.addAttribute("errCode","⚠️");
	    			model.addAttribute("errMsg","Sorry,marketplace active seller limit exceeded...");
	    			return "UserPages/error";
	        	}
	        	
	        	apiService.requestToManageecomToSaveMarketplace(map);
			    activeSellerRepository.save(seller);
			}else {
				model.addAttribute("errCode","⚠️");
				model.addAttribute("errMsg","This seller already active.");
				return "UserPages/error";
			}
        	
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",400);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
		return "redirect:/marketplace/getSeller?status=pending&page=0";
		
	}

	public String deactivateTheSeller(String id, Principal p, Model model) {
		try {
			Seller seller = activeSellerRepository.findByEmailAddr(id);
			if(seller!=null) {
				activeSellerRepository.delete(seller);
				Map<String,String> map=new HashMap();
	        	ObjectMapper objectMapper = new ObjectMapper();
	        	MarketplaceAdmin user = marketplaceAdminRepository.findByEmail(p.getName());
	        	map.put("marketplaceEmail",user.getEmail());
	        	map.put("sellerEmail",id);
	        	apiService.requestToManageecomToRemoveMarketplace(map);

			}
			return "redirect:/marketplace/getSeller?status=active&page=0";
		}catch(Exception e){
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
		
	}

	
	public String getSellerProducts(String sellerId,int totalProducts,int page,int limit,Model model) {
		try {
	        ObjectMapper objectMapper = new ObjectMapper();
			model.addAttribute("sellerProducts",objectMapper.readValue(productApis.getProductsBySellerId(sellerId, page, limit), new TypeReference<List<Products>>() {}));
			model.addAttribute("totalPage",(int) Math.ceil((double) totalProducts / limit));
			model.addAttribute("currentPage", page);
			model.addAttribute("totalProducts",totalProducts);
			model.addAttribute("sellerId",sellerId);
			return "UserPages/sellerProductsTable";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	
	@Override
	public String editSellerMOV(String sellerEmail, int mov) {
		try {
			logger.info("change mov value of "+sellerEmail+" | "+mov);
			Seller seller = activeSellerRepository.findByEmailAddr(sellerEmail);
			if(seller!=null){
				seller.setMinimumOrderValue(mov);
				activeSellerRepository.save(seller);
				return "0";
			}
			return "1";
			
		}catch(Exception e) {
			logger.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return "-1";
		}
	}
}
