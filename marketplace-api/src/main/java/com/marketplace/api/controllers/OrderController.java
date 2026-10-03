package com.marketplace.api.controllers;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.api.entitys.Coupon;
import com.marketplace.api.entitys.OrderDetails;
import com.marketplace.api.entitys.OrderProducts;
import com.marketplace.api.entitys.PaymentDetails;
import com.marketplace.api.entitys.RecentlyViewedProducts;
import com.marketplace.api.entitys.ReturnDetails;
import com.marketplace.api.entitys.ReturnPolicy;
import com.marketplace.api.entitys.User;
import org.json.JSONObject;
import com.marketplace.api.jwtHelper.JwtUtil;
import com.marketplace.api.repository.OrderDetailsRepository;
import com.marketplace.api.repository.OrderProductRepository;
import com.marketplace.api.repository.RecentlyViewedProductsRepository;
import com.marketplace.api.repository.ReturnDetailsRepository;
import com.marketplace.api.repository.ReturnPolicyRepository;
import com.marketplace.api.repository.UserRepository;
import com.marketplace.api.serviceClasses.CouponService;
import com.marketplace.api.serviceClasses.OrderDetailsService;


@RestController
public class OrderController {
	Logger logger = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
	@Autowired
	private OrderProductRepository orderProductRepository;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ReturnPolicyRepository returnPolicyRepository;
	@Autowired
	private ReturnDetailsRepository returnDetailsRepository;
	@Autowired
	private RecentlyViewedProductsRepository recentlyViewedProductsRepository;
	@Autowired
	OrderDetailsService orderDetailsService;
	@Autowired
	CouponService couponService;
	
	
	
	//this method is used for save the new order
	@PostMapping("/saveOrderDetails")
	public String saveTheOrderDetailsHere(@RequestBody OrderDetails orderDetails,@RequestHeader(HttpHeaders.AUTHORIZATION)String token) {
		String result=null;
		try {
			//System.out.println("Email -> "+jwtUtil.getUsernameFromToken(token.substring(7)));
			User user= userRepository.findByEmail(jwtUtil.getUsernameFromToken(token.substring(7)).split("#!#")[0]);
			//System.out.println("Token user : "+user.getFirstName());
		   // result= orderDetailsService.saveOrder(o, user);
			
		//	System.out.println("########  "+orderDetails.toString());
			result= orderDetailsService.PhonepayAPIIntegrationSaveOrder(orderDetails,user);
		    
			return result;	
		}catch(Exception e) {
			e.printStackTrace();
			return result;
		}
	 }
	
	
	@PostMapping("/updateOrder")
	public String updateOrderPaymentStatus(@RequestBody Map<String,Object> map,@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		String email=jwtUtil.getUsernameFromToken(jwtToken.substring(7)).split("#!#")[0];
		String result =orderDetailsService.updateOrderPaymentStatus((String)map.get("txid"),email);
		return result;
	}
	
	
	
	@GetMapping("/getUserDetails")
	public String getUserDatails(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		String email=jwtUtil.getUsernameFromToken(jwtToken.substring(7));
		// Use findByEmailWithOrders to load complete order hierarchy with JOIN FETCH
		// This avoids lazy-loading issues during JSON serialization
		User user=userRepository.findByEmailWithOrders(email.split("#!#")[0]);
		user.setPassword(null);
		
		ObjectMapper mapper=new ObjectMapper();
		try {
		    String json=mapper.writeValueAsString(user);
		//    String json2=mapper.writeValueAsString(orders);
			return json;

		}catch(Exception e) {
			logger.error("error : "+e.getMessage());
			return e.getMessage();
		}
	}
	
	@GetMapping("/getOrderDetails")
	public List getOrderDetails(@RequestParam("page") int page){
		logger.info("getorderDetails call...");
		Pageable pageble = PageRequest.of(page, 5);
		List<OrderDetails> orders=new ArrayList<>();
		for(OrderDetails ord:orderDetailsRepository.findAll(pageble)) {
			orders.add(ord);
		}
		return orders;
	}
	
	@GetMapping("/getTotalCountOfOrders")
	public String getTotalCountOfOrders() {
		int count=orderDetailsRepository.findAll().size();
		return ""+count;
	}
	
	@GetMapping("/getReturnDetails")
	public List getReturnProductDetails(@RequestParam("page") int page){
		Pageable pageble = PageRequest.of(page, 5);
		List<ReturnDetails> list=new ArrayList<>();
		for(ReturnDetails rds:returnDetailsRepository.findAll(pageble)) {
			list.add(rds);
		}
		return list;
	}
	
	@GetMapping("/getTotalCountOfReturnDetails")
	public String getTotalCountOfReturnDetails() {
		int count=returnDetailsRepository.findAll().size();
		return ""+count;
	}
	
	//user try to return the order or product
	@PostMapping("/returnPolicyByUser")
	public String returnProductByUser(@RequestBody ReturnDetails rds) {
		//System.out.println("orderId : "+rds.getOrderProductId()+" categoryId : "+rds.getCategoryId()+" payment id : "+rds.getPaymentId()+" reason : "+rds.getReason()+" amt : "+rds.getAmt());
		try {
			OrderProducts order=orderProductRepository.findById(rds.getOrderProductId()).get();
			ReturnPolicy returnPolicy=returnPolicyRepository.findByCategoryId(rds.getCategoryId());
	
			//calculate the diffrence between orderdate or todays date
			LocalDate orderDate=LocalDate.of(order.getOrderDate().getYear(),order.getOrderDate().getMonth(),order.getOrderDate().getDate());
			Date date=new Date();
			LocalDate todaysDate=LocalDate.of(date.getYear(),date.getMonth(),date.getDate());
			Period difDay=Period.between(orderDate, todaysDate);
			
			if(difDay.getDays() <= returnPolicy.getPolicyDays()) {
				rds.setStatus("pending");
				rds.setReturnOrderDtTime(date);
				returnDetailsRepository.save(rds);
				order.setUnderReturnPolicy("1");
				orderProductRepository.save(order);
				return "0";
			}else {
				return "1";
			}
		}catch(Exception e) {
			System.out.println("### ERROR hapen in returnPolicyByUser ###");
			e.printStackTrace();
			return "-1";
		}	
	}
	
	//shipping partner (ithinklogistics)
	@PostMapping("/getOrderDetailsInJson/{orderProductId}")
	public ResponseEntity<?> orderDetailsInJSON(@PathVariable("orderProductId") String orderProductId) {
		try {
			OrderProducts order=orderProductRepository.findById(orderProductId).get();
			System.out.println("done : "+order.toString());

			if(order!=null) {
				return ResponseEntity.status(HttpStatus.OK).body(Map.of("status",0,"message","done","data",order.toString()));
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status",1,"message","Invalid id !!","data",null));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status",-1,"message",e.getMessage(),"data",null));
		}
	}
	
	//user -> marketplace web
	@PostMapping("/getOrderProductDetailsInJson/{orderProductId}")
	public String orderProductDetailsInJSON(@PathVariable("orderProductId") String orderProductId) {
		try {
			System.out.println("done : "+orderProductId);
			OrderProducts order=orderProductRepository.findById(orderProductId).get();
			return order.toString();
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//apply CouponPojo
	@PostMapping("/applyCoupon")
	public ResponseEntity<?> validateCoupon(@RequestBody Map<String,String> map){
		//write here conditions of coupon
		JSONArray jsonArray = new JSONArray(map.get("products"));
		return couponService.validateCoupon(jsonArray,map.get("coupon"),Integer.parseInt(map.get("amount")));
	}
	

	@GetMapping("/getDefaultCoupon")
	public ResponseEntity<?> getDefaultCoupon(){
		return couponService.getDefaultCouponCode();
	}
	

	//update orderProduct status from manage-ecom seller
	@GetMapping("/updateOrderProductStatus")
	private ResponseEntity<?> updateOrderProductStatus(@RequestParam("orderProductId") String orderProductId , @RequestParam("status") String status) {
		return orderDetailsService.updateOrderproductStatus(orderProductId, status);
	}

	/**
	 * RAZORPAY ENDPOINTS
	 * These endpoints handle Razorpay payment integration
	 */

	/**
	 * Create Razorpay Order - Called after marketplace order creation
	 * Endpoint: POST /createRazorpayOrder
	 * 
	 * Request body: {
	 *   "orderId": "OD-xxxxx"
	 * }
	 * 
	 * Response: {
	 *   "status": 0,
	 *   "razorpayOrderId": "order_xxxxx",
	 *   "razorpayKeyId": "rzp_test_xxxxx" or "rzp_live_xxxxx",
	 *   "amount": 500,
	 *   "currency": "INR"
	 * }
	 */
	@PostMapping("/createRazorpayOrder")
	public ResponseEntity<?> createRazorpayOrder(@RequestBody Map<String, String> requestBody,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		try {
			String orderId = requestBody.get("orderId");
			String email = jwtUtil.getUsernameFromToken(jwtToken.substring(7)).split("#!#")[0];

			logger.info("Razorpay order creation requested for: " + orderId);

			JSONObject result = orderDetailsService.createRazorpayOrderRequest(orderId);
			return ResponseEntity.ok(result.toMap());

		} catch (Exception e) {
			logger.error("Error creating Razorpay order: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", -1, "message", "Error creating Razorpay order"));
		}
	}

	/**
	 * Verify Razorpay Payment - Called after user completes payment
	 * Endpoint: POST /verifyRazorpayPayment
	 * 
	 * Request body: {
	 *   "razorpay_order_id": "order_xxxxx",
	 *   "razorpay_payment_id": "pay_xxxxx",
	 *   "razorpay_signature": "xxxxx"
	 * }
	 * 
	 * Response: {
	 *   "status": 0,
	 *   "message": "Payment verified successfully",
	 *   "orderId": "OD-xxxxx"
	 * }
	 */
	@PostMapping("/verifyRazorpayPayment")
	public ResponseEntity<?> verifyRazorpayPayment(@RequestBody Map<String, String> requestBody,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		try {
			String razorpayOrderId = requestBody.get("razorpay_order_id");
			String razorpayPaymentId = requestBody.get("razorpay_payment_id");
			String razorpaySignature = requestBody.get("razorpay_signature");
			String email = jwtUtil.getUsernameFromToken(jwtToken.substring(7)).split("#!#")[0];

			logger.info("Razorpay payment verification requested - OrderId: " + razorpayOrderId + ", PaymentId: "
					+ razorpayPaymentId);

			JSONObject result = orderDetailsService.verifyRazorpayPayment(razorpayOrderId, razorpayPaymentId,
					razorpaySignature, email);
			return ResponseEntity.ok(result.toMap());

		} catch (Exception e) {
			logger.error("Error verifying Razorpay payment: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", -1, "message", "Error verifying payment"));
		}
	}

	/**
	 * Razorpay Webhook Endpoint
	 * Endpoint: POST /razorpayWebhook
	 * 
	 * Header: X-Razorpay-Signature: signature
	 * Body: Razorpay webhook event JSON
	 * 
	 * This endpoint processes Razorpay payment events asynchronously
	 */
	@PostMapping("/razorpayWebhook")
	public ResponseEntity<?> razorpayWebhook(@RequestBody String webhookBody,
			@RequestHeader(value = "X-Razorpay-Signature", required = false) String webhookSignature) {
		try {
			logger.info("Razorpay webhook received");

			if (webhookSignature == null || webhookSignature.isEmpty()) {
				logger.warn("Webhook signature missing");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", -1, "message", "Missing signature"));
			}

			JSONObject result = orderDetailsService.handleRazorpayWebhook(webhookBody, webhookSignature);
			return ResponseEntity.ok(result.toMap());

		} catch (Exception e) {
			logger.error("Error processing Razorpay webhook: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", -1, "message", "Webhook processing error"));
		}
	}
	
	/**
	 * Add a product to recently viewed list
	 * Endpoint: POST /addRecentlyViewedProduct
	 * 
	 * Request body: {
	 *   "productId": "prod-123",
	 *   "productName": "Product Name",
	 *   "productPrice": 999.99,
	 *   "productImage": "image-url",
	 *   "categoryId": 5
	 * }
	 */
	@PostMapping("/addRecentlyViewedProduct")
	public ResponseEntity<?> addRecentlyViewedProduct(
			@RequestBody Map<String, Object> payload,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		try {
			String email = jwtUtil.getUsernameFromToken(jwtToken.substring(7));
			User user = userRepository.findByEmail(email.split("#!#")[0]);
			
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("status", -1, "message", "User not found"));
			}
			
			String productId = (String) payload.get("productId");
			if (productId == null || productId.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status", -1, "message", "Product ID is required"));
			}
			
			// Check if product already in recently viewed (to avoid duplicates)
			RecentlyViewedProducts existing = recentlyViewedProductsRepository.findByUserAndProductId(user, productId);
			if (existing != null) {
				// Update the viewed date
				existing.setViewedDate(new Date());
				recentlyViewedProductsRepository.save(existing);
				return ResponseEntity.ok(Map.of("status", 0, "message", "Product view updated"));
			}
			
			// Add new recently viewed product
			RecentlyViewedProducts rvp = new RecentlyViewedProducts();
			rvp.setUser(user);
			rvp.setProductId(productId);
			rvp.setProductName((String) payload.get("productName"));
			rvp.setProductPrice(((Number) payload.getOrDefault("productPrice", 0)).floatValue());
			rvp.setProductImage((String) payload.getOrDefault("productImage", ""));
			rvp.setCategoryId(((Number) payload.getOrDefault("categoryId", 0)).intValue());
			rvp.setViewedDate(new Date());
			
			recentlyViewedProductsRepository.save(rvp);
			return ResponseEntity.ok(Map.of("status", 0, "message", "Product added to recently viewed"));
			
		} catch (Exception e) {
			logger.error("Error adding recently viewed product: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", -1, "message", e.getMessage()));
		}
	}
	
	/**
	 * Get recently viewed products for authenticated user
	 * Endpoint: GET /getRecentlyViewedProducts
	 * 
	 * Response: List<RecentlyViewedProducts> sorted by most recently viewed first
	 */
	@GetMapping("/getRecentlyViewedProducts")
	public ResponseEntity<?> getRecentlyViewedProducts(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		try {
			String email = jwtUtil.getUsernameFromToken(jwtToken.substring(7));
			User user = userRepository.findByEmail(email.split("#!#")[0]);
			
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("status", -1, "message", "User not found"));
			}
			
			List<RecentlyViewedProducts> recentlyViewed = recentlyViewedProductsRepository.findRecentlyViewedByUser(user.getUserId());
			return ResponseEntity.ok(Map.of("status", 0, "data", recentlyViewed));
			
		} catch (Exception e) {
			logger.error("Error fetching recently viewed products: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", -1, "message", e.getMessage()));
		}
	}

}
