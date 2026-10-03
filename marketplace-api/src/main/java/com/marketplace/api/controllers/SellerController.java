package com.marketplace.api.controllers;

import java.io.File;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.api.entitys.Address;
import com.marketplace.api.entitys.BillingAddress;
import com.marketplace.api.entitys.ProductRatings;
import com.marketplace.api.entitys.ReportAbuse;
import com.marketplace.api.entitys.ReportingAccount;
import com.marketplace.api.entitys.Seller;
import com.marketplace.api.entitys.ShippingAddress;
import com.marketplace.api.entitys.User;
import com.marketplace.api.jwtAuthentication.UserCustomConfigService;
import com.marketplace.api.jwtHelper.JwtUtil;
import com.marketplace.api.repository.ProductRatingsRepository;
import com.marketplace.api.repository.ReportAbuseRepositoy;
import com.marketplace.api.repository.ReportingAccountRepo;
import com.marketplace.api.repository.SellerRepository;
import com.marketplace.api.repository.UserRepository;
import com.marketplace.api.serviceClasses.ApiCalls;
import com.marketplace.api.serviceClasses.EmailActions;
import com.marketplace.api.serviceClasses.MarketplaceAdminSetting;
import com.marketplace.api.serviceClasses.SortMap;
import com.marketplace.api.services.GoogleTokenVerificationService;
import com.marketplace.api.util.CacheHandler;
import com.marketplace.api.util.ProductApis;

@RestController
public class SellerController {
	Logger logger = LoggerFactory.getLogger(SellerController.class);
	@Autowired
	SellerRepository sellerRepository;
	@Autowired
	private UserCustomConfigService userCustomConfigService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	ApiCalls apiCalls;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	SortMap sortMap;
	@Autowired
	EmailActions emailActions;
	@Autowired
	MarketplaceAdminSetting marketplaceAdminSetting;
	@Autowired
	ReportingAccountRepo reportingAccountRepo;
	@Autowired
	ProductRatingsRepository productRatingsRepository;
	@Autowired
	CacheHandler cacheHandler;
	@Autowired
	ReportAbuseRepositoy  reportAbuseRepository;
	@Autowired
	GoogleTokenVerificationService googleTokenVerificationService;
	@Autowired
	ProductApis productApis;
	
	@Value("${marketplaceId}")
	int marketplaceId;
	
	@Value("${report.abuse.images.path}")
	String REPORT_ABUSE_IMAGES_PATH;
	
	@PostConstruct
	public void init() {
		try {
			logger.info("marketplace starting...");
			marketplaceAdminSetting.setSettings(marketplaceId);
			logger.info("Marketplace ID : "+marketplaceId);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/getActiveSellers")
	public List<Seller> getActiveSellers() {
		List<Seller> activeSellers = sellerRepository.findAll();
		return activeSellers;
	}
	
	//manageecom seller request
	@PostMapping("/saveSellerRequest")
	public int saveSellerRequest(@RequestBody Map<String,String>map) {
		try {
			Seller s=null;
			 s = sellerRepository.findByEmailAddr(map.get("sellerEmail"));
			if(s==null) {
				s=new Seller();
				s.setApiEndpoint(map.get("sellerApiEndPoint"));
				s.setFirstName(map.get("sellerFname"));
				s.setLastName(map.get("sellerLname"));
				s.setEmailAddr(map.get("sellerEmail"));
				s.setPhoneNumber(map.get("sellerContact"));
				s.setNameOfCompany(map.get("sellerNameOfComapany"));
				s.setStatus("seller_request");//seller request to marketplace
				s.setRegisteredSellerId(Integer.parseInt(map.get("sellerId")));
				sellerRepository.save(s);
				return 0;
			}else {
				
				if(s.getStatus().equalsIgnoreCase("pending")) {
					return 1;
				}else if(s.getStatus().equalsIgnoreCase("seller_request")) {
					return 2;//
				}
				return 3;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@GetMapping("/getActiveSellersEndpoint")
	public List<String> getActiveSellersEndpoints() {
		List<String> apiEndpoints = new ArrayList<>();
		for (Seller s : sellerRepository.findAll()) {
			apiEndpoints.add(s.getApiEndpoint());
		}
		return apiEndpoints;
	}
	
	
	@GetMapping("/products")
	public String getAllProducts(
			@RequestParam(value = "_limit",required = false, defaultValue = "12") String limit,
			@RequestParam(value = "_start", required = false, defaultValue = "0") String offset,
			@RequestParam(value = "title_contains", required = false) String title,
			@RequestParam(required = false, value = "price_gt", defaultValue = "0.0f") String minPrice,
			@RequestParam(required = false, value = "price_lt", defaultValue = "0.0f") String maxPrice,
			@RequestParam(required = false, value = "action", defaultValue = "Nan") String action) {
		
		try {
			if(offset.equalsIgnoreCase("undefined"))
				offset="0";
			if(limit.equalsIgnoreCase("undefined"))
				limit="12";
			if(minPrice.equalsIgnoreCase("undefined"))
				minPrice="0f";
			if(maxPrice.equalsIgnoreCase("undefined"))
				maxPrice="0f";
			
			/*
			 * _start parameter is the offset, not the page number.
			 * Use it directly without calculation.
			 */
			int from = Integer.parseInt(offset);
			Map<String,Object> searchMap=new HashMap<>();
			searchMap.put("from", from);
			searchMap.put("limit", Integer.parseInt(limit));
			searchMap.put("minPrice", Float.parseFloat(minPrice));
			searchMap.put("maxPrice", Float.parseFloat(maxPrice));
			searchMap.put("action", action);
			if(action.equalsIgnoreCase("lowToHigh"))
				searchMap.put("sortOrder", "ASC");
			else if(action.equalsIgnoreCase("highToLow"))
				searchMap.put("sortOrder", "DESC");
			else
				searchMap.put("sortOrder", null);
			
			searchMap.put("sortBy","sale_price");
			return productApis.getProducts(title,searchMap);
			
//			if(IS_ELASTICSEARCH_ENABLE) {
//				return productHelper.getElasticsearchServiceProducts().getAllProducts(title, searchMap);
//			}else {
//				return productHelper.getCustomServiceProducts().getAllProducts(title, searchMap);
//			}
			
		} catch (Exception e) {
			System.out.println("error happen : " + e.getMessage());
			e.printStackTrace();
			return "[]";
		}
	}
	
	@GetMapping("/getProductVariations")
	public String getProductVariations(@RequestParam("variationId") String variationId) {
		return productApis.getProductVariations(variationId);
	}

	@GetMapping("/searchRecords")
public String searchTheRecords(
        @RequestParam(value = "title_contains", required = false) String title,
        @RequestParam(value = "_limit", required = false, defaultValue = "12") String limit,
        @RequestParam(value = "_start", required = false, defaultValue = "1") String page) {

    try {

        if (page.equalsIgnoreCase("undefined")) {
            page = "1";
        }

        if (limit.equalsIgnoreCase("undefined")) {
            limit = "12";
        }

        int from = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);

        Map<String, Object> searchMap = new HashMap<>();

        searchMap.put("minPrice", 0.0f);
        searchMap.put("maxPrice", 0.0f);
        searchMap.put("sortOrder", "");
        searchMap.put("limit", Integer.parseInt(limit));
        searchMap.put("from", from);
        searchMap.put("sortBy", "");

        System.out.println("### SEARCH ###");
        System.out.println("title = " + title);
        System.out.println("page = " + page);
        System.out.println("limit = " + limit);
        System.out.println("from = " + from);

        return productApis.getProductsBySimilarTitleOrKeywords(
                title,
                searchMap
        );

    } catch (Exception e) {

        e.printStackTrace();
        return "[]";
    }
}

	
	@GetMapping("/getAllProducts")
	public String getAllActiveSellerproduct() {
		return productApis.getActiveSellerProducts();
//		if(IS_ELASTICSEARCH_ENABLE) {
//			return productHelper.getElasticsearchServiceProducts().getAllActiveSellerproduct();
//		}else {
//			return productHelper.getCustomServiceProducts().getAllActiveSellerproduct();
//		}
	}

	@GetMapping("products/{productId}")
	public String getProductById(@PathVariable("productId") String productId) {
		return productApis.getProductById(productId);
//		if(IS_ELASTICSEARCH_ENABLE) {
//			return productHelper.getElasticsearchServiceProducts().getProductById(productId);
//		}else {
//			return productHelper.getCustomServiceProducts().getProductById(productId);
//		}
	}

	@GetMapping("product-categories")
	public String getProductBySlug(@RequestParam("slug") String categoryId ,
		@RequestParam(value = "_limit",required = false, defaultValue = "12") String limit,
		@RequestParam(value = "_start", required = false, defaultValue = "1") String page,
		@RequestParam(required = false, value = "price_gt", defaultValue = "0f") String minPrice,
		@RequestParam(required = false, value = "price_lt", defaultValue = "0f") String maxPrice,
		@RequestParam(required = false, value = "action", defaultValue = "Nan") String action) {
	try {

			if(page.equalsIgnoreCase("undefined"))
				page="1";
			if(limit.equalsIgnoreCase("undefined"))
				limit="12";
			if(minPrice.equalsIgnoreCase("undefined"))
				minPrice="0f";
			if(maxPrice.equalsIgnoreCase("undefined"))
				maxPrice="0f";
			
			int from = (Integer.parseInt(page)  - 1) * Integer.parseInt(limit);
			Map<String,Object> searchMap=new HashMap<>();
			searchMap.put("from",  from);
			searchMap.put("limit", Integer.parseInt(limit));
			searchMap.put("minPrice",  Float.parseFloat(minPrice));
			searchMap.put("maxPrice", Float.parseFloat(maxPrice));
			
			if(action.equalsIgnoreCase("lowToHigh"))
				searchMap.put("sortOrder", "ASC");
			else if(action.equalsIgnoreCase("highToLow"))
				searchMap.put("sortOrder", "DESC");
			else
				searchMap.put("sortOrder", null);
			
			searchMap.put("sortBy","sale_price");
			return productApis.getProductsByCategory(categoryId,searchMap);
//			if(IS_ELASTICSEARCH_ENABLE) {
//				return productHelper.getElasticsearchServiceProducts().getProductBySlug(categoryId, searchMap);
//			}else {
//				return productHelper.getCustomServiceProducts().getProductBySlug(categoryId, searchMap);
//			}
			
		} catch (Exception e) {
			System.out.println("Error hapen (Categorys) : " + e.getMessage());
			e.printStackTrace();
			return "[]";

		}

	}

	@GetMapping("brands")
	public String getProductByBrand(@RequestParam("slug") String brand,
			@RequestParam(value = "_limit",required = false, defaultValue = "12") String limit,
			@RequestParam(value = "_start", required = false, defaultValue = "1") String page,
			@RequestParam(required = false, value = "price_gt", defaultValue = "0") String minPrice,
			@RequestParam(required = false, value = "price_lt", defaultValue = "0") String maxPrice,
			@RequestParam(required = false, value = "action", defaultValue = "Nan") String action) {
		try {
			
			if(page.equalsIgnoreCase("undefined"))
				page="1";
			if(limit.equalsIgnoreCase("undefined"))
				limit="12";
			if(minPrice.equalsIgnoreCase("undefined"))
				minPrice="0f";
			if(maxPrice.equalsIgnoreCase("undefined"))
				maxPrice="0f";
			
			
			  int from = (Integer.parseInt(page) - 1) * 10;
				Map<String,Object> searchMap=new HashMap<>();
				searchMap.put("from", from);
				searchMap.put("limit", Integer.parseInt(limit));
				searchMap.put("minPrice", Float.parseFloat(minPrice));
				searchMap.put("maxPrice", Float.parseFloat(maxPrice));
				
				if(action.equalsIgnoreCase("lowToHigh"))
					searchMap.put("sortOrder", "ASC");
				else if(action.equalsIgnoreCase("highToLow"))
					searchMap.put("sortOrder", "DESC");
				else
					searchMap.put("sortOrder", null);
				
				searchMap.put("sortBy","sale_price");
				return productApis.getProductsByBrand(brand, searchMap);
//				if(IS_ELASTICSEARCH_ENABLE) {
//					return productHelper.getElasticsearchServiceProducts().getProductByBrand(brand, searchMap);
//				}else {
//					return productHelper.getCustomServiceProducts().getProductByBrand(brand, searchMap);
//				}

		}catch (Exception e) {
			System.out.println("Error (brands) : " + e.getMessage());
			e.printStackTrace();
			return "[]";
		}

	}

	@GetMapping("products/count")
public int getProductCount(
    @RequestParam(value = "_key", required = false, defaultValue = "") String key,
    @RequestParam(value = "_value", required = false, defaultValue = "") String value,
    @RequestParam(value = "title_contains", required = false, defaultValue = "") String title,
    @RequestParam(value = "price_gt", required = false, defaultValue = "0.0") String minPrice,
    @RequestParam(value = "price_lt", required = false, defaultValue = "0.0") String maxPrice) {

    try {

        if (
                minPrice == null ||
                minPrice.equalsIgnoreCase("undefined") ||
                minPrice.trim().isEmpty()
        ) {
            minPrice = "0.0";
        }

        if (
                maxPrice == null ||
                maxPrice.equalsIgnoreCase("undefined") ||
                maxPrice.trim().isEmpty()
        ) {
            maxPrice = "0.0";
        }

        float minimumPrice =
                Float.parseFloat(minPrice);

        float maximumPrice =
                Float.parseFloat(maxPrice);

        Map<String, Object> searchMap =
                new HashMap<>();

        searchMap.put(
                "minPrice",
                minimumPrice
        );

        searchMap.put(
                "maxPrice",
                maximumPrice
        );

        searchMap.put(
                "key",
                key
        );

        searchMap.put(
                "value",
                value
        );
if (title != null && !title.trim().isEmpty()) {
    searchMap.put("title", title.trim());
} else if (
        "title_keyword".equalsIgnoreCase(key)
        && value != null
        && !value.trim().isEmpty()
) {
    searchMap.put("title", value.trim());
} else {
    searchMap.put("title", "");
}

        System.out.println(
                "### PRODUCT COUNT SEARCH MAP ### "
                + searchMap
        );

        String result =
                productApis.getProductCount(
                        searchMap
                );

        return Integer.parseInt(result);

    } catch (Exception e) {

        e.printStackTrace();

        return 0;
    }
}

	@GetMapping("productsByIds")
	public String getProductsByIds(@RequestParam("id_in") List<String> ids) {
		return productApis.getProductsByIds(ids);
//		if(IS_ELASTICSEARCH_ENABLE) {
//			return productHelper.getElasticsearchServiceProducts().getProductsByIds(ids);
//		}else {
//			return productHelper.getCustomServiceProducts().getProductsByIds(ids);
//		}
	}
	
	
	@PostMapping("saveProductRating")
	private ResponseEntity<?> saveProductRatings(@RequestBody Map<String,String> map,@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		try {
			User user= userRepository.findByEmail(jwtUtil.getUsernameFromToken(jwtToken.substring(7,jwtToken.length())));
			Seller s=sellerRepository.findByEmailAddr(map.get("sellerId"));
			if(s!=null && user!=null) {
				ProductRatings pr=productRatingsRepository.findByUserIdAndProductId(user.getUserId(), map.get("productId"));
				if(pr==null) {
					pr=new ProductRatings();
					apiCalls.updateRatingRatio(s.getApiEndpoint()+"/updateProductRating",Map.of("productId",map.get("productId"),"rating",Float.parseFloat(map.get("rate")+""),"increment",1));
				}else {
					float rate=-(pr.getRating()-Float.parseFloat(map.get("rate")));
					apiCalls.updateRatingRatio(s.getApiEndpoint()+"/updateProductRating",Map.of("productId",map.get("productId"),"rating",rate,"increment",0));
				}
				pr.setUserId(user.getUserId());
				pr.setReview(map.get("review"));
				pr.setProductId(map.get("productId"));
				pr.setRating(Float.parseFloat(map.get("rate")+""));
				pr.setUserName(user.getFirstName()+" "+user.getLastName());
				productRatingsRepository.save(pr);
				return ResponseEntity.ok(Map.of("status",0,"message","rating save."));
			}else
			return ResponseEntity.badRequest().body(Map.of("status",1,"message","Rating not save."));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("status",-1,"message",e.getMessage()));
		}
	}

	@GetMapping("/getRelatedProduct")
	public String getRelatedProductByKeywords(@RequestParam("keywords") String keywords) {
		return productApis.getProductsBySimilarTitleOrKeywords(keywords, Map.of("minPrice",0.0f,"maxPrice",0.0f,"sortOrder","","limit",20,"from",0,"sortBy",""));
//		if(IS_ELASTICSEARCH_ENABLE) {
//			return productHelper.getElasticsearchServiceProducts().getRelatedProductByKeywords(keywords);
//		}else {
//			return productHelper.getCustomServiceProducts().getRelatedProductByKeywords(keywords);
//		}
	}

	// ################################################################################
	// main controller code
	// login process for get the token for authentication api
	@PostMapping("/login")
	public ResponseEntity<String> generateToken(@RequestBody User user) throws Exception {
		try {
			// System.out.println("user login details "+user.getEmail()+" |
			// "+user.getPassword()+" | "+user.getLoginGateway());

			if (user.getLoginGateway().equalsIgnoreCase("normal_account")) {
				user.setEmail(user.getEmail() + "#!#normal_account");
			}
			if (user.getLoginGateway().equalsIgnoreCase("affiliate_account")) {
				user.setEmail(user.getEmail() + "#!#affiliate_account");
			}

			this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("### Invalid username!!");
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			return ResponseEntity.ok("{\"message\" : \"Invalid email or password..\",\"status\":1}");
			// throw new Exception("### Bad credentials!!");
		}
		// generate the token
		UserDetails userDetails = this.userCustomConfigService.loadUserByUsername(user.getEmail());
		String token = this.jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok("{ \"Token\" : \"" + token + "\", \"status\":0}");

	}

	/**
	 * Google OAuth Login/Registration endpoint
	 * Verifies Google ID token and creates/authenticates user
	 * Preserves affiliate_account_id from referral links
	 */
	@PostMapping("/googleLogin")
	public ResponseEntity<?> googleLogin(@RequestBody Map<String, Object> request) {
		try {
			String credential = (String) request.get("credential");
			Object affiliateObj = request.get("affiliate_account_id");
			int affiliateAccountId = 0;
			
			if (credential == null || credential.isEmpty()) {
				return ResponseEntity.ok(Map.of("status", 1, "message", "Missing Google credential"));
			}
			
			// Parse affiliate_account_id if provided
			if (affiliateObj != null) {
				if (affiliateObj instanceof Number) {
					affiliateAccountId = ((Number) affiliateObj).intValue();
				} else if (affiliateObj instanceof String) {
					try {
						affiliateAccountId = Integer.parseInt((String) affiliateObj);
					} catch (NumberFormatException e) {
						return ResponseEntity.ok(Map.of("status", 1, "message", "Invalid affiliate account ID"));
					}
				}
			}
			
			// Verify affiliate account if provided
			if (affiliateAccountId > 0) {
				ReportingAccount affiliate = reportingAccountRepo.findByReportingAccountId(affiliateAccountId);
				if (affiliate == null) {
					return ResponseEntity.ok(Map.of("status", 1, "message", "Invalid affiliate account"));
				}
			}
			
			// Verify Google token with Google's servers
			JSONObject verifiedClaims = googleTokenVerificationService.verifyGoogleToken(credential);
			
			if (verifiedClaims.getInt("status") != 0) {
				logger.warn("Google token verification failed: " + verifiedClaims.optString("error"));
				return ResponseEntity.ok(Map.of("status", 1, "message", verifiedClaims.optString("error", "Invalid Google token")));
			}
			
			String googleId = verifiedClaims.getString("sub");
			String email = verifiedClaims.getString("email");
			String firstName = verifiedClaims.optString("given_name", "");
			String lastName = verifiedClaims.optString("family_name", "");
			
			logger.info("Processing Google login for email: " + email + " with google_id: " + googleId);
			
			// Check if user already has Google ID linked
			User user = userRepository.findByGoogleId(googleId);
			
			if (user != null) {
				// Existing Google user - login
				logger.info("Existing Google user found: " + user.getEmail());
			} else {
				// Check if email exists but without Google ID
				user = userRepository.findByEmail(email);
				
				if (user != null) {
					// Existing email user without Google ID - link Google to existing account
					logger.info("Linking Google ID to existing user: " + email);
					user.setGoogleId(googleId);
					userRepository.save(user);
				} else {
					// New user - create new account
					logger.info("Creating new Google user: " + email);
					user = new User();
					user.setEmail(email);
					user.setGoogleId(googleId);
					user.setFirstName(firstName.isEmpty() ? email.split("@")[0] : firstName);
					user.setLastName(lastName.isEmpty() ? "Google User" : lastName);
					user.setPhone(""); // Will be filled during checkout
					// Set a placeholder password (Google users don't use passwords)
					user.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
					user.setRole("ROLE_NORMAL");
					user.setLoginGateway("google");
					
					// Preserve affiliate attribution
					user.setReportingAccountId(affiliateAccountId);
					
					userRepository.save(user);
					logger.info("New Google user created: " + email + " with reportingAccountId: " + affiliateAccountId);
				}
			}
			
			// Generate JWT token using existing authentication mechanism
			// Append #!#google to email for consistent JWT handling with login types
			UserDetails userDetails = this.userCustomConfigService.loadUserByUsername(user.getEmail() + "#!#google");
			String token = this.jwtUtil.generateToken(userDetails);
			
			logger.info("Google login successful for user: " + user.getEmail());
			return ResponseEntity.ok("{ \"Token\" : \"" + token + "\", \"status\":0}");
			
		} catch (Exception e) {
			logger.error("Google login error: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("status", 1, "message", "Google login failed"));
		}
	}

	// save the user registration here
	@PostMapping("/saveUser")
	public ResponseEntity<?> saveUserRegistrationDetails(@RequestBody User user) {
		try {
			if (user.getFirstName().equals("") || user.getLastName().equals("")) {
				return ResponseEntity.ok().body(Map.of("status",-1,"message","invalid first name or last name."));
			}
			if (user.getEmail().equals("") || !user.getEmail().matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
				return ResponseEntity.ok().body(Map.of("status",-2,"message","invalid email id."));
			}
			if (user.getPassword().length() < 4) {
				return ResponseEntity.ok().body(Map.of("status",-3,"message","invalid password."));
			}
			if (user.getPhone().length() < 10 && !user.getPhone().matches("^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$")) {
				return ResponseEntity.ok().body(Map.of("status",-4,"message","invalid phone number."));

			}
			if (userRepository.findByEmail(user.getEmail()) != null) {
				return ResponseEntity.ok().body(Map.of("status",-5,"message","email id alredy exist.."));
			}
			
			// Validate reportingAccountId if provided
			if (user.getReportingAccountId() > 0) {
				ReportingAccount affiliate = reportingAccountRepo.findByReportingAccountId(user.getReportingAccountId());
				if (affiliate == null) {
					return ResponseEntity.ok().body(Map.of("status",-6,"message","invalid affiliate account."));
				}
			}
			
			user.setRole("ROLE_NORMAL");
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			// generate the token
			UserDetails userDetails = this.userCustomConfigService.loadUserByUsername(user.getEmail());
			String token = this.jwtUtil.generateToken(userDetails);
			
			return ResponseEntity.ok(Map.of("status",0,"token",token));
		} catch (Exception e) {
			logger.error("ERROR  :" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("status",-1,"message",e.getMessage()));
		}
	}

	@PostMapping("/updateUserDetails")
	public String updateUserDetailsMethod(@RequestBody User updateUserDetails,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		User user = userRepository.findByEmail(jwtUtil.getUsernameFromToken(jwtToken.substring(7)));

		if (updateUserDetails.getEmail() != null && !updateUserDetails.getEmail().equals("")) {
			user.setEmail(updateUserDetails.getEmail());
		}
		if (updateUserDetails.getFirstName() != null && !updateUserDetails.getFirstName().equals("")) {
			user.setFirstName(updateUserDetails.getFirstName());
		}
		if (updateUserDetails.getLastName() != null && !updateUserDetails.getLastName().equals("")) {
			user.setLastName(updateUserDetails.getLastName());
		}
		if (updateUserDetails.getPhone() != null && updateUserDetails.getPhone().length() == 10) {
			user.setPhone(updateUserDetails.getPhone());
		}
//				if(updateUserDetails.getShippingAddress()!=null && !updateUserDetails.getShippingAddress().equals("")) {
//					user.setShippingAddress(updateUserDetails.getShippingAddress());
//				}
//				if(updateUserDetails.getCity()!=null && !updateUserDetails.getCity().equals("")) {
//					user.setCity(updateUserDetails.getCity());
//				}
//				if(updateUserDetails.getCountry()!=null && !updateUserDetails.getCountry().equals("")) {
//					user.setCountry(updateUserDetails.getCountry());
//				}
		if (updateUserDetails.getPassword() != null && !updateUserDetails.getPassword().equals("")) {
			user.setPassword(bCryptPasswordEncoder.encode(updateUserDetails.getPassword()));
		}
//				if(!updateUserDetails.getState().equalsIgnoreCase("Nan")) {
//					user.setState(updateUserDetails.getState());
//				}

		// System.out.println(""+updateUserDetails.getEmail()+" |
		// "+updateUserDetails.getFirstName()+" "+updateUserDetails.getLastName());
		userRepository.save(user);
		return "0";
	}

	@PostMapping("/saveAddress")
	public ResponseEntity<Object> saveUserAddresses(@RequestBody Map<String, Object> map,@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		try {
			Address address = null;
			if (map.get("type").toString().equals("billing-address")) {
				address = new BillingAddress();
			} else if (map.get("type").toString().equals("shipping-address")) {
				address = new ShippingAddress();
			}
			if (address == null) {
				System.out.println("address is nulll");
				return new ResponseEntity<Object>("null", HttpStatus.OK);
			}
			address.setFirstName(map.get("firstName").toString());
			address.setLastName(map.get("lastName").toString());
			address.setCountry(map.get("country").toString());
			address.setStreetAddress(map.get("streetAddress").toString());
			address.setState(map.get("state")+"");
			address.setEmail(map.get("email").toString());
			address.setCity(map.get("city").toString());
			address.setPincode(map.get("pincode").toString());

			//User user = userRepository.findByEmail(map.get("loginUserEmail").toString());
			User user=userRepository.findByEmail(jwtUtil.getUsernameFromToken(jwtToken.substring(7)));
			if (map.get("type").toString().equals("billing-address")) {
				user.setBillingAddress((BillingAddress) address);
			} else if (map.get("type").toString().equals("shipping-address")) {
				user.setShippingAddress((ShippingAddress) address);
			}

			address.setUser(user);
			userRepository.save(user);
			return new ResponseEntity<Object>("done", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/forgotePasswordCreateId")
	public ResponseEntity<Object> createResetPasswordIdAndSendEmailToUser(@RequestBody Map<String, Object> map) {
		try {
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			User user = userRepository.findByEmail((String) map.get("emailId"));
			if (user == null) {
				return new ResponseEntity<Object>("wrong_input", HttpStatus.OK);
			} else {
				user.setResetPasswordVerificationId(id);
				userRepository.save(user);
				boolean flag = emailActions.sendResetPasswordEmailMessage(user, id);
				if (flag)
					return new ResponseEntity<Object>("done", HttpStatus.OK);
				else
					return new ResponseEntity<Object>("Email not send", HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<Object> resetPassword(@RequestBody Map<String, Object> map) {
		try {
			String verificationId = (String) map.get("verificationId");
			String password = (String) map.get("password");

			if (verificationId.length() > 5 && password.length() > 0) {
				User user = userRepository.findByResetPasswordVerificationId(verificationId);
				if (user != null) {
					user.setPassword(bCryptPasswordEncoder.encode(password));
					user.setResetPasswordVerificationId(null);
					userRepository.save(user);
					return new ResponseEntity<Object>("done", HttpStatus.OK);
				} else {
					return new ResponseEntity<Object>("invalid inputs", HttpStatus.BAD_REQUEST);
				}

			} else {
				return new ResponseEntity<Object>("invalid inputs", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// #############################################################################
	
	//manageecom seller change the status of marketplace it save in marketplace db
	@PostMapping("/changeSellerStatus")
	public ResponseEntity<?> changeSellerStatus(@RequestBody Map<String,String> map){
		try {
			Seller seller=sellerRepository.findByEmailAddr(map.get("sellerEmail"));
			if(seller!=null) {
				//status active | inactive
				seller.setStatus(map.get("status"));
				seller.setRegisteredSellerId(Integer.parseInt(map.get("userId")));
				sellerRepository.save(seller);
				return ResponseEntity.ok("seller status changed.");
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid seller id");
			}
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/getReportingAccountDetails")
	public List<ReportingAccount> getReportingAccountDetails(){
		List<ReportingAccount> list=new ArrayList<>();
		for(ReportingAccount accnt : reportingAccountRepo.findAll()) {
			accnt.setAddress(null);
			accnt.setEmail(null);
			accnt.setPassword(null);
			accnt.setPhone(null);
			accnt.setRole(null);
			list.add(accnt);
		}
		return list;
	}
	
	/**
	 * affilate marketing == reporting account 
	 * affilate marketing = marketplace_web
	 * reporting account = marketplace_admin
	 * */
	
	@PostMapping("/saveAffilateAccountDatails")
	private ResponseEntity<?> saveAffilateAccountDetails(@RequestBody ReportingAccount reportingAccount){
		try {
			ReportingAccount ra=reportingAccountRepo.findByEmail(reportingAccount.getEmail());
			if(ra==null) {
				reportingAccount.setPassword(bCryptPasswordEncoder.encode(reportingAccount.getPassword()));
				reportingAccount.setRole("ROLE_NORMAL");
				reportingAccountRepo.save(reportingAccount);
				return ResponseEntity.ok(Map.of("status",0,"message","affilate account details save successfully."));
			}else {
				return ResponseEntity.ok(Map.of("status",1,"message",reportingAccount.getEmail()+" is already present, use another email id."));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("status",-1,"message",e.getMessage()));
		}
	}
	
	@GetMapping("/getAffiliateAccountDetails")
	private ResponseEntity<?> getAffiliateAccountDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken){
		try {
			ReportingAccount ac= reportingAccountRepo.findByEmail(jwtUtil.getUsernameFromToken(jwtToken.substring(7)));
			if(ac!=null) {
				logger.info("affiliate account log in ID : "+ac.getReportingAccountId());
				return ResponseEntity.ok(Map.of("status",0,"affiliate_account",ac));
			}else {
				return ResponseEntity.ok().body(Map.of("status",-1,"message","invalid email!!"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("status",0,"message",e.getMessage()));
		}
	}
	
	@GetMapping("/updateProductCache")
	public ResponseEntity<?> updateProductCache(){
		try {
			cacheHandler.setUpData();
			return ResponseEntity.ok(Map.of("status","success","message","product cache updated."));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("status","error","message",e.getMessage()));
		}
	}
	
	@PostMapping("/saveReportAbuseData")
	public ResponseEntity<?> saveReportAbuseData(@RequestParam("reportData")String data ,@RequestParam(name="file",required = false) MultipartFile file ){
		try {
			JSONObject obj=new JSONObject(data);
			ReportAbuse reportAbuse=new ReportAbuse();
			reportAbuse.setName(obj.getString("name"));
			if(file!=null) {
				String name=UUID.randomUUID().toString().replace("-", "")+".jpg";
	            file.transferTo(new File(REPORT_ABUSE_IMAGES_PATH +"/"+ name));
	            reportAbuse.setEvidenceFileName(name);
			}
			
			reportAbuse.setEmail(obj.getString("email"));
			reportAbuse.setSubject(obj.getString("subject"));
			reportAbuse.setNatureOfAbuse(obj.getString("natureOfAbuse"));
			reportAbuse.setIncidentDtTime(obj.getString("incidentDtTime"));
			reportAbuse.setDesc(obj.getString("desc"));
			reportAbuse.setActionRequested(obj.getString("actionRequested"));
			
			reportAbuseRepository.save(reportAbuse);
			return ResponseEntity.ok(Map.of("status",0,"message","your report is save successfully."));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("status",-1,"message",e.getMessage()));
		}
	}
	
	@GetMapping("/getMarketplaceSetupInfo")
	public Map<String,Object> getMarketplaceAdminStatus() {
		try {
			return marketplaceAdminSetting.getMarketplaceSetupInfo();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	
}
