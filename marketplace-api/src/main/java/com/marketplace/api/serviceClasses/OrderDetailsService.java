package com.marketplace.api.serviceClasses;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;


import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.marketplace.api.HelperClasses.OrderProductsPojo;
import com.razorpay.Order;
import com.marketplace.api.entitys.Coupon;
import com.marketplace.api.entitys.CouponUsers;
import com.marketplace.api.entitys.Invoice;
import com.marketplace.api.entitys.OrderDetails;
import com.marketplace.api.entitys.OrderProducts;
import com.marketplace.api.entitys.PaymentDetails;
import com.marketplace.api.entitys.ReferralOrders;
import com.marketplace.api.entitys.Seller;
import com.marketplace.api.entitys.Settlement;
import com.marketplace.api.entitys.User;
import com.marketplace.api.entitys.MarketplaceAdmin;
import com.marketplace.api.repository.CouponRepository;
import com.marketplace.api.repository.CouponUsersRepository;
import com.marketplace.api.repository.OrderDetailsRepository;
import com.marketplace.api.repository.OrderProductRepository;
import com.marketplace.api.repository.PaymentDetailsRepository;
import com.marketplace.api.repository.ReferralOrdersRepository;
import com.marketplace.api.repository.ReturnDetailsRepository;
import com.marketplace.api.repository.SellerRepository;
import com.marketplace.api.repository.SettlementRepository;
import com.marketplace.api.repository.UserRepository;
import com.marketplace.api.util.CacheHandler;
import com.marketplace.api.util.ProductApis;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@CrossOrigin
public class OrderDetailsService {
	Logger logger=LoggerFactory.getLogger(OrderDetailsService.class);
	@Autowired
	UserRepository userRepository;
	@Autowired
	ReturnDetailsRepository returnDetailsRepository;
	@Autowired
	OrderDetailsRepository orderDetailsRepository;
	@Autowired
	OrderProductRepository orderProductRepository;
	@Autowired
	PaymentDetailsRepository paymentDetailsRepository;
	@Autowired
	MarketplaceAdminSetting marketplaceAdminSetting;
	@Autowired
	EmailActions emailAction;
	@Autowired
	SellerRepository sellerRepository;
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	CouponUsersRepository couponUsersRepository;
	@Autowired
	CouponService couponService;
	@Autowired
	SettlementRepository settlementRepository;
	@Autowired
	ProductApis productApis;
	@Autowired
	ReferralOrdersRepository referralOrdersRepository;
	@Autowired
	RazorpayService razorpayService;
	@PersistenceContext
    private EntityManager entityManager;

	
	@Autowired
	CacheHandler cacheHandler;

	@Value("${marketplace.web.base.url}")
	String BASE_URL;
	@Value("${marketplace.api.base.url}")
	String marketplaceApiUrl;
	
	@Value("${production.level}")
	boolean IS_PRODUCTION;

	
	int MARKETPLACE_CHARGE=15;
	int SERVICE_CHARGE=5;
	
	public OrderProductsPojo getOrderProductPojo(List<OrderProducts> ops) {
		try {
			OrderProductsPojo opp=new OrderProductsPojo();
			Map<String,JSONObject> map=new HashMap<>();
			float totalPrice=0;
			for(OrderProducts op:ops) {
				 JSONObject jsonObject = new JSONObject(productApis.getProductById(op.getId()));
				 totalPrice+=jsonObject.getInt("sale_price")*op.getQuantity();
				 map.put(op.getId(), jsonObject);
			}
			opp.setProductMapJson(map);
			opp.setTotalPrice(totalPrice);
			return opp;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public String PhonepayAPIIntegrationSaveOrder(OrderDetails orderDetails, User user) {
		String result=null;
		int SHIPPING_FEE=49;
		
		
		
		try {
			Map<String,Object> marketplaceSetupInfo=marketplaceAdminSetting.getMarketplaceSetupInfo();
			OrderProductsPojo orderProductsPojo=getOrderProductPojo(orderDetails.getProducts());
			
			if(orderDetails.getPaymentType().equals("Cash on Delivery")) {
				if(marketplaceAdminSetting.getCodStatus().equalsIgnoreCase("disabled")) {
					return "cash_on_delivery_not_available";
				}
			}
			
			//System.out.println("GSTIN : "+orderDetails.getGstin()+" | "+marketplaceSetupInfo.get("businessType"));
;
			if(marketplaceSetupInfo.get("businessType").toString().equals("b2b")) {
				if(orderDetails.getGstin().length()==0) {
					return "gstin_required";
				}
			}
			
			if((int)marketplaceSetupInfo.get("codPrepaidAmountPercentage")!=orderDetails.getCodPrepaidAmountPercentage()) {
				logger.error("ui codPrepaidAmountPercentage : "+orderDetails.getCodPrepaidAmountPercentage()+" | server codPrepaidAmountPercentage : "+marketplaceSetupInfo.get("codPrepaidAmountPercentage")+" - is not match.");
				return "cod_prepaid_persentage_not_match";
			}
			
			
			float couponDiscountOrderPrice=0f;
			boolean isCouponApplied=false;
			String discountedProductIds[]=null;
			float couponDiscount=0;
			
			//if coupon is present 
			if(orderDetails.getCouponCode()!=null) {
				
				Coupon coupon=couponRepository.findByCouponCode(orderDetails.getCouponCode());
				if(coupon.getCondions()!=null) {
					for(String condition : coupon.getCondions().split(",")) {
						if(condition.trim().equals("One-time use per customer")) {
							if(couponUsersRepository.countByCouponCodeAndUserId(orderDetails.getCouponCode(),user.getEmail())>0) {
								return "user_alredy_use_coupon";
							}
						}
					}
				}
				
				ObjectMapper mapper = new ObjectMapper();
		        mapper.enable(SerializationFeature.INDENT_OUTPUT);
		        String json = mapper.writeValueAsString(orderDetails.getProducts());
		        JSONArray jsonArray = new JSONArray(json);
		        
				ResponseEntity<?> resen=couponService.validateCoupon(jsonArray,orderDetails.getCouponCode(),(int)orderProductsPojo.getTotalPrice());
				JSONObject couponResponse =new JSONObject(resen.getBody().toString().replaceAll("=", ":"));
				if(couponResponse.getInt("status")==0) {
					isCouponApplied=true;
					couponDiscountOrderPrice=couponResponse.getFloat("discountedPrice");
					discountedProductIds = couponResponse.getString("discountedProductIds").split(" ");
					couponDiscount=couponResponse.getFloat("couponDiscount");
				}else {
					return "InvalidCouponCode";
				}
			}
			
			logger.info(user.getFirstName()+" is trying to save order.");
			MarketplaceAdmin integratinSetting = marketplaceAdminSetting.getSettings();
			String merchantId = integratinSetting.getPhonpe_merchantId();
			String saltIndex = integratinSetting.getPhonpe_index();
			String saltKey = integratinSetting.getPhonpe_saltKey();
			String PHONPE_BASE_URL = integratinSetting.getPhonpe_url();
			
			
			Response response=null;
			String orderId = "OD-" + UUID.randomUUID().toString().replaceAll("-", "");
			int totalOrderPrice = 0;
	
			// store the new clean order here
			List<OrderProducts> list = new ArrayList<>();
			// main order
			OrderDetails saveOrder = new OrderDetails();
			saveOrder.setOrderId(orderId);
			
			
			
			//separate the order products by seller and store in map(seller_email_id , list_of_products)
			Map<Integer, List<JSONObject>> map = new HashMap<>();
			for (OrderProducts p : orderDetails.getProducts()) {
				
			
				JSONObject json =orderProductsPojo.getProductMapJson().get(p.getId());
				int sellerEmailId = json.getInt("userId");
				json.put("Qty", p.getQuantity());
				if (map.containsKey(sellerEmailId)) {
					List<JSONObject> l = map.get(sellerEmailId);
					l.add(json);
					map.put(sellerEmailId, l);
				} else {
					List<JSONObject> l = new ArrayList<>();
					l.add(json);
					map.put(sellerEmailId, l);
				}
			}
			
			
			List<Invoice> invoiceList = new ArrayList<>();
			for (Entry<Integer, List<JSONObject>> entry : map.entrySet()) {
				// invoice create here..
				Invoice invoice = new Invoice();
	
				String taxType = null;// CGST SGST IGST
				String state = orderDetails.getState();
				Seller s = sellerRepository.findByRegisteredSellerId(entry.getKey());
				int totalAmtSeller=0;
				
				if (state.equalsIgnoreCase(s.getState())) {
					taxType = "CGST,SGST";
				} else {
					taxType = "IGST";
				}
	
				invoice.setTaxType(taxType);
				invoice.setOrderId(orderId);
				invoice.setInvoiceOrderId(saveOrder);
				invoice.setOrderStatus("processing");
				logger.info("invoice generating...");
				List<OrderProducts> invoiceItems = new ArrayList<>();

				//getting seller all products
				for (JSONObject jsonObject : entry.getValue()) {
					OrderProducts product = new OrderProducts();
					product.setOrderProductId(UUID.randomUUID().toString().replace("-", ""));
					
					// Handle stockDetails safely - it may be null, JSONObject.NULL, or a valid JSONObject
					Object stockDetailsRaw = jsonObject.get("stockDetails");
					logger.info("Processing product " + jsonObject.optString("id", "unknown") + 
							" - stockDetails is " + (stockDetailsRaw == null || JSONObject.NULL.equals(stockDetailsRaw) ? "NULL/missing" : "present"));
					
					// Only process stockDetails if it's a valid JSONObject
					if (stockDetailsRaw != null && !JSONObject.NULL.equals(stockDetailsRaw)) {
						try {
							JSONObject stockDetails = new JSONObject(stockDetailsRaw.toString());
							if(stockDetails.isNull("sku")) {
								product.setSku(stockDetails.getString("comboSku"));
								product.setWarehouseId(stockDetails.getInt("warehouse_id"));
							} else {
								product.setSku(stockDetails.getString("sku"));
								product.setWarehouseId(stockDetails.getInt("warehouse_id"));
							}
						} catch (Exception e) {
							logger.warn("Failed to parse stockDetails for product " + jsonObject.optString("id", "unknown") + 
									": " + e.getMessage() + ". Using fallback values.");
							// Use product SKU as fallback if stockDetails parsing fails
							product.setSku(jsonObject.optString("sku", ""));
							product.setWarehouseId(jsonObject.optInt("warehouseId", 0));
						}
					} else {
						logger.warn("stockDetails is missing/null for product " + jsonObject.optString("id", "unknown") + 
								". Using fallback SKU from product data.");
						// Use fallback values from product JSON itself
						product.setSku(jsonObject.optString("sku", ""));
						product.setWarehouseId(jsonObject.optInt("warehouseId", 0));
					}
				
					product.setProduct_name(jsonObject.getString("title"));
					product.setCategoryId(jsonObject.getInt("categoryId"));
					int productSalePrice = jsonObject.getInt("sale_price");
					int sellerId = jsonObject.getInt("userId");
					product.setSellerId(sellerId);
					int qty = jsonObject.getInt("Qty");
					String pid = jsonObject.getString("productRefId");
	
					// int taxRate = jsonObject.getInt("hsnRate");
					int taxRate = 12;
					float taxAmt = productSalePrice * taxRate / (100 + taxRate);

					product.setTaxAmt(taxAmt * qty);
					product.setTaxRate(taxRate + "");
	
					product.setSole_price(productSalePrice);
					
					if(isCouponApplied) {
						for(String discountProductId : discountedProductIds) {
							if(pid.equals(discountProductId)) {
								product.setCouponCode(orderDetails.getCouponCode());
								product.setSole_price((int)couponService.calculateCouponDiscountPrice(productSalePrice, couponDiscount));
							}
						}
					}
					
					totalOrderPrice += productSalePrice * qty;
					totalAmtSeller+= productSalePrice * qty;
					
					product.setId(pid);
					product.setStatus("pending");
					product.setUnderReturnPolicy("0");
					product.setOrderDate(new Date());
					product.setQuantity(qty);

					product.setInvoiceDetails(invoice);
					list.add(product);
					invoiceItems.add(product);
					
				}
				logger.info("MOV => "+s.getEmailAddr()+" |  "+totalAmtSeller+" | "+s.getMinimumOrderValue());
//				if(s.getMinimumOrderValue()==null)
//					s.setMinimumOrderValue(10);//default minimum value
				if(totalAmtSeller < Optional.ofNullable(s.getMinimumOrderValue()).orElse(0)) {
					logger.info("Your order from "+s.getFirstName()+" "+s.getLastName()+" needs to be at least "+s.getMinimumOrderValue()+" to proceed. You currently have "+totalAmtSeller+" in your cart. Please add items worth "+(s.getMinimumOrderValue()-totalAmtSeller)+" more to your cart to meet the minimum order requirement.");
					return "Your order from "+s.getFirstName()+" "+s.getLastName()+" needs to be at least â‚¹"+s.getMinimumOrderValue()+" to proceed. You currently have â‚¹"+totalAmtSeller+" in your cart. Please add items worth â‚¹"+(s.getMinimumOrderValue()-totalAmtSeller)+" more to your cart to meet the minimum order requirement.";
				}
				invoice.setItems(invoiceItems);
				invoiceList.add(invoice);
			}

			saveOrder.setFname(orderDetails.getFname());
			saveOrder.setLname(orderDetails.getLname());
			saveOrder.setCity(orderDetails.getCity());
			saveOrder.setState(orderDetails.getState());
			saveOrder.setGstin(orderDetails.getGstin());
			saveOrder.setTotalProducts(orderDetails.getProducts().size());
			saveOrder.setUser(user);
			// saveOrder.setProductNames(productName);
			saveOrder.setContactNumber(orderDetails.getContactNumber());
			saveOrder.setShippingAddressPrimary(orderDetails.getShippingAddressPrimary());
			saveOrder.setLandmark(orderDetails.getLandmark());
			saveOrder.setPostalCode(orderDetails.getPostalCode());
			if(isCouponApplied) {
				saveOrder.setCouponCode(orderDetails.getCouponCode());
				couponDiscountOrderPrice +=SHIPPING_FEE;//50 rs is shipping fee
				saveOrder.setTotalBill(couponDiscountOrderPrice);// discounted total price
			}else {
				saveOrder.setCouponCode(null);
				totalOrderPrice+=SHIPPING_FEE;//50 rs is shipping fee
				saveOrder.setTotalBill(totalOrderPrice);// total price
			}
			saveOrder.setPaymentStatus("unpaid");
			saveOrder.setCodPrepaidPaymentStatus("unpaid");
			saveOrder.setCodPrepaidAmountPercentage((int)marketplaceSetupInfo.get("codPrepaidAmountPercentage"));
			saveOrder.setOrderDate(new Date());
			saveOrder.setOrderStatus("active");
			saveOrder.setPaymentType(orderDetails.getPaymentType());
			saveOrder.setShippingFee(SHIPPING_FEE);
			
			// saveOrder.setProducts(list);
			
			saveOrder.setInvoices(invoiceList);
			user.getOrders().add(saveOrder);
			logger.debug("save order into db.");
			userRepository.save(user);
			
			if(isCouponApplied) {
				totalOrderPrice = (int)couponDiscountOrderPrice;// discounted total price
			}
			if(orderDetails.getPaymentType().equals("Online Payment")) {
				// RAZORPAY AS PRIMARY GATEWAY
				if(razorpayService.isConfigured()) {
					logger.info("(ONLINE PAYMENT) Using Razorpay as payment gateway for order: "+orderId);
					JSONObject razorpayResponse = new JSONObject();
					razorpayResponse.put("status", "razorpay");
					razorpayResponse.put("orderId", orderId);
					razorpayResponse.put("amount", totalOrderPrice);
					razorpayResponse.put("currency", "INR");
					razorpayResponse.put("message", "Order created. Proceed to Razorpay payment.");
					result = razorpayResponse.toString();
					logger.info("(ONLINE PAYMENT) razorpay response : "+result);
				} else {
					// FALLBACK TO PHONPE IF RAZORPAY NOT CONFIGURED
					logger.warn("(ONLINE PAYMENT) Razorpay not configured, falling back to PhonePe");
					try {
						String payload = "{\r\n" + "	\"merchantId\": \"" + merchantId + "\",\r\n"
								+ "	\"merchantTransactionId\": \"" + orderId + "\",\r\n"
								+ "	\"merchantUserId\": \"MUID181\",\r\n" + "	\"amount\": " + totalOrderPrice * 100 + ",\r\n"
								+ "	\"redirectUrl\": \"" + BASE_URL + "/api/OrderRoute?flag=1&actionType=orderUpdatestatus&txid="
								+ orderId + "\",\r\n" + "	\"redirectMode\": \"POST\",\r\n"
								+ "	\"callbackUrl\": \"http://nuvio.in:8089/test\",\r\n" + "	\"mobileNumber\": \""
								+ user.getPhone() + "\",\r\n" + "	\"paymentInstrument\": {\r\n"
								+ "		\"type\": \"PAY_PAGE\"\r\n" + "	}\r\n" + "}";
			
						String base64 = Base64.getEncoder().encodeToString(payload.getBytes());
						String sha256hex = DigestUtils.sha256Hex(base64 + "/pg/v1/pay" + saltKey);
			
						OkHttpClient client = new OkHttpClient();
			
						MediaType mediaType = MediaType.parse("application/json");
						okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\"request\":\"" + base64 + "\"}");
						Request request = new Request.Builder().url(PHONPE_BASE_URL + "/pg/v1/pay").post(body)
								.addHeader("accept", "application/json").addHeader("Content-Type", "application/json")
								.addHeader("X-VERIFY", sha256hex + "###" + saltIndex).build();
			
						response = client.newCall(request).execute();
						result= response.body().string();
						logger.info("(ONLINE PAYMENT) phonepe response : "+result);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					} finally {
						if(response!=null) {
							response.close();
						}
					}
				}
			}else {
				
				if(saveOrder.getCodPrepaidAmountPercentage()>0) {
				//////////////////////////////////
					int prepaidCodAmt = (int)((saveOrder.getCodPrepaidAmountPercentage() / 100.0) * (totalOrderPrice-SHIPPING_FEE));
				
					try {
						logger.info("COD PRE ORDER STARTING >> orderId: "+saveOrder.getOrderId()+" | %: "+saveOrder.getCodPrepaidAmountPercentage()+" | total_price including shipping : "+totalOrderPrice+" unpaid : "+((saveOrder.getCodPrepaidAmountPercentage() / 100) * totalOrderPrice));
						//System.out.println("paid amt :"+prepaidCodAmt);
						String payload = "{\r\n" + "	\"merchantId\": \"" + merchantId + "\",\r\n"
								+ "	\"merchantTransactionId\": \"" + orderId + "\",\r\n"
								+ "	\"merchantUserId\": \"MUID181\",\r\n" + "	\"amount\": " + prepaidCodAmt * 100 + ",\r\n"
								+ "	\"redirectUrl\": \"" + BASE_URL + "/api/OrderRoute?flag=1&actionType=orderUpdatestatus&txid="
								+ orderId + "\",\r\n" + "	\"redirectMode\": \"POST\",\r\n"
								+ "	\"callbackUrl\": \"http://nuvio.in:8089/test\",\r\n" + "	\"mobileNumber\": \""
								+ user.getPhone() + "\",\r\n" + "	\"paymentInstrument\": {\r\n"
								+ "		\"type\": \"PAY_PAGE\"\r\n" + "	}\r\n" + "}";
			
						String base64 = Base64.getEncoder().encodeToString(payload.getBytes());
						String sha256hex = DigestUtils.sha256Hex(base64 + "/pg/v1/pay" + saltKey);
			
						OkHttpClient client = new OkHttpClient();
			
						MediaType mediaType = MediaType.parse("application/json");
						okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\"request\":\"" + base64 + "\"}");
						Request request = new Request.Builder().url(PHONPE_BASE_URL + "/pg/v1/pay").post(body)
								.addHeader("accept", "application/json").addHeader("Content-Type", "application/json")
								.addHeader("X-VERIFY", sha256hex + "###" + saltIndex).build();
			
						response = client.newCall(request).execute();
						result= response.body().string();
						logger.info("(COD PRE PAYMENT) phonepe response : "+result);
		
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}finally {
						if(response!=null) {
							response.close();
						}
					}
				
				///////////////////////////////////
				}else {
					
					OrderDetails savedOrderDetails = orderDetailsRepository.findByOrderId(saveOrder.getOrderId());
					saveOrderIntoManageEcom(savedOrderDetails);
					createSettelment(savedOrderDetails);
					saveReferralOrderDetails(savedOrderDetails);
					
					if(IS_PRODUCTION)
						emailAction.sendOrderSuccessEmail(user.getEmail(), saveOrder);
					logger.info("COD ORDER SAVED");
					result = "cod_order_save";
				}
			}
			return result;
		}catch(Exception e) {
			logger.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	

	public String updateOrderPaymentStatus(String merchantTransactionId, String email) {
		try {

			logger.info("payment status update call - merchantTransactionId : "+merchantTransactionId+" email : "+email);
			MarketplaceAdmin integratinSetting = marketplaceAdminSetting.getSettings();
			String merchantId = integratinSetting.getPhonpe_merchantId();
			String saltIndex = integratinSetting.getPhonpe_index();
			String saltKey = integratinSetting.getPhonpe_saltKey();
			String PHONPE_BASE_URL = integratinSetting.getPhonpe_url();
			// System.out.println("BASE_URL : "+PHONPE_BASE_URL);

			String sha256hex = DigestUtils
					.sha256Hex("/pg/v1/status/" + merchantId + "/" + merchantTransactionId + saltKey);

			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url(PHONPE_BASE_URL + "/pg/v1/status/" + merchantId + "/" + merchantTransactionId).get()
					.addHeader("accept", "application/json").addHeader("Content-Type", "application/json")
					.addHeader("X-VERIFY", sha256hex + "###" + saltIndex).addHeader("X-MERCHANT-ID", merchantId)
					.build();

			// System.out.println("Request :: "+request.toString());
			Response response = client.newCall(request).execute();
			
			
			String jres = response.body().string();
			JSONObject jsonObject = new JSONObject(jres);
			String paymentCode = (String) jsonObject.get("code");
			response.close();
			// System.out.println("Response :: "+jsonObject.toString());
			OrderDetails order = orderDetailsRepository.findById(merchantTransactionId).get();

			logger.info("PAYMENT_CODE : "+paymentCode+" | order_payment_status : "+order.getPaymentStatus()+" | order_payment_type :"+order.getPaymentType()+" | order_id : "+order.getOrderId());
			
			if(paymentCode.equals("PAYMENT_SUCCESS") && order.getCodPrepaidPaymentStatus().equalsIgnoreCase("unpaid") && order.getPaymentType().equals("Cash on Delivery") ) {
				//this block for cod prepaid orders
				if(order.getCodPrepaidPaymentStatus()==null || !order.getCodPrepaidPaymentStatus().equals("success")) {
					logger.info("cod prepaid order saving in manageecom...");
					order.setCodPrepaidPaymentStatus("success");
					order.setPaymentGatewayStatus(jsonObject.toString());
					
					if(order.getCouponCode()!=null) {
						CouponUsers user=new CouponUsers();
						user.setCouponCode(order.getCouponCode());
						user.setUserId(order.getUser().getEmail());
						couponUsersRepository.save(user);
					}
					
					orderDetailsRepository.save(order);
					saveOrderIntoManageEcom(order);
					createSettelment(order);
					saveReferralOrderDetails(order);
					
					if(IS_PRODUCTION)
					emailAction.sendOrderSuccessEmail(email, order);
					return "0";
				}else
					return "0";
			}else if (paymentCode.equals("PAYMENT_SUCCESS") && order.getPaymentStatus().equalsIgnoreCase("unpaid") && !order.getPaymentType().equals("Cash on Delivery")) {
				//this block for online orders
				if(order.getOrderStatus()==null || !order.getPaymentStatus().equals("success")) {
					logger.info("online order saving in manageecom...");
					order.setPaymentStatus("success");
					order.setPaymentGatewayStatus(jsonObject.toString());
					
					if(order.getCouponCode()!=null) {
						CouponUsers user=new CouponUsers();
						user.setCouponCode(order.getCouponCode());
						user.setUserId(order.getUser().getEmail());
						couponUsersRepository.save(user);
					}
					
					orderDetailsRepository.save(order);
					saveOrderIntoManageEcom(order);
					createSettelment(order);
					
					saveReferralOrderDetails(order);
					if(IS_PRODUCTION)
					emailAction.sendOrderSuccessEmail(email, order);
					return "0";
				}else
					return "0";
			} else if (!paymentCode.equals("PAYMENT_SUCCESS")
					&& !order.getPaymentStatus().equalsIgnoreCase("success")) {
				order.setPaymentStatus("failed");
				order.setPaymentGatewayStatus(jsonObject.toString());
				orderDetailsRepository.save(order);
				logger.warn("payment status failed!!");
				return "1";
			} else {
				return "2";
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Error Info : " + e.getMessage());
			e.printStackTrace();
			return "-1";
		}
	}

	/**
	 * Refund the order by marketplace admin or seller refund payment by razorpay
	 * never used
	 */
	/*public String refundThePlacedOrder(int returnDetailsId) {
		try {

			ReturnDetails rds = returnDetailsRepository.findById(returnDetailsId).get();
			RazorpayClient razorpay = new RazorpayClient(RZ_KEY, RZ_TEST_KEY);
			System.out.println("Payment id : " + rds.getPaymentId());
			String paymentId = rds.getPaymentId();

			JSONObject refundRequest = new JSONObject();
			refundRequest.put("amount", rds.getAmt());
			refundRequest.put("speed", "normal");
			JSONObject notes = new JSONObject();
			notes.put("notes_key_1", "nuvio payment");
			// notes.put("notes_key_2","Tea, Earl Greyâ€¦ decaf.");
			refundRequest.put("notes", notes);
			// update recipt number here
			refundRequest.put("receipt", "Receip no " + new Random().nextInt(10000));

			Refund r = razorpay.payments.refund(paymentId, refundRequest);
			rds.setRefundRZstatus("" + r);
			rds.setStatus("refunded");
			returnDetailsRepository.save(rds);
			return "0";
		} catch (Exception e) {
			System.out.println("ERROR(refund order service ) : " + e.getMessage());
			e.printStackTrace();
			return "-1";
		}
	}*/

	private boolean saveOrderIntoManageEcom(OrderDetails orderDetails) {
		//Response response=null;
		try {
			logger.info("("+orderDetails.getOrderId()+") order details saving in manageEcom...");
			//OkHttpClient client = new OkHttpClient();
			//MediaType mediaType = MediaType.parse("application/json");

			Map<String, Object> map = new HashMap<>();
			map.put("buyerName", orderDetails.getFname() + " " + orderDetails.getLname());
			map.put("buyerContact", orderDetails.getContactNumber());
			map.put("address", orderDetails.getShippingAddressPrimary() + ", " + orderDetails.getCity() + ", "
					+ orderDetails.getState() + ", " + orderDetails.getPostalCode());
			map.put("state", orderDetails.getState());
			map.put("city",orderDetails.getCity());
			map.put("pincode",orderDetails.getPostalCode());
			map.put("paymentStatus", orderDetails.getPaymentStatus());
			map.put("codPrepaidAmountPercentage", orderDetails.getCodPrepaidAmountPercentage());
			map.put("codPrepaidPaymentStatus", orderDetails.getCodPrepaidPaymentStatus());
			map.put("orderPaymentType", orderDetails.getPaymentType());
			map.put("shippingFee", 49);
			MarketplaceAdmin ma = marketplaceAdminSetting.getSettings();
			map.put("marketplaceEmail", ma.getEmail());
			map.put("marketplaceName", ma.getCompanyName());
			map.put("marketplaceUri", marketplaceApiUrl);
			map.put("orderId",orderDetails.getOrderId());
			map.put("paymentStatus", orderDetails.getPaymentStatus());
			map.put("gstin", orderDetails.getGstin());
			map.put("productListSize",orderDetails.getTotalProducts());
			User user=orderDetails.getUser();
			map.put("buyerName",user.getFirstName()+" "+user.getLastName());
			map.put("buyerEmail", user.getEmail());
			map.put("buyerContact", user.getPhone());
			//map.put("orderDate", orderDetails.getOrderDate());
			
			//invoice related error handel by set, size not to get proper (it give duble count)
			Set<Integer> set=new HashSet<>();
			
			for(Invoice invoice: orderDetails.getInvoices()) {
				
				if(set.contains(invoice.getInvoiceId())) {
					continue;
				}else {
					set.add(invoice.getInvoiceId());
				}
				Seller s = sellerRepository.findByRegisteredSellerId(invoice.getItems().get(0).getSellerId());

//				10 :[{"order_product_id":"1d3d79f78c394e9eaafebda21f155dba", "product_id":"01371b9fcbce49919872706f92408fba", "categoryId":"10", "quantity":"1", "sole_price":"498", "product_name":"Style Eva Cycling Motorbike ATV/Dirt Bike Racing Goggles with Detachable Mask Adjustable Strap Sunglasses", "status":"pending", "orderDate":"2023-12-07 12:09:51.373", "underReturnPolicy":"0", "invoiceDetails":{"invoiceId":10," orderId":"OD-082a8efcb1954ffb8bf4c0eb8abe91ec", "taxType":"IGST" ,"orderDetails":{"orderId":"OD-082a8efcb1954ffb8bf4c0eb8abe91ec", "fname":"rohit", "lname":"k", "shippingAddressPrimary":"test", "state":"Manipur", "landmark":"null", "city":"nashik", "postalCode":"123321", "orderDate":"2023-12-07 12:09:51.373", "contactNumber":"9702990419", "paymentStatus":"success", "orderStatus":"active", "razorpayOrderDetails":null, "token":"null", "totalBill":"697.0", "paymentDetails":null,"user":{"userId":"8", "firstName":"rohit", "lastName":"k", "phone":"+919702990419", "email":"rohit@gmail.com", "shippingAddress":"com.marketplace.api.entitys.ShippingAddress@1cef1a7", "billingAddress":"com.marketplace.api.entitys.BillingAddress@39a3ac3e" , "role":"ROLE_NORMAL"},"products":"[]"}} ,"sellerId":"nuviosellers@gmail.com"}, {"order_product_id":"5144098624e045c88e1267e54c2df989", "product_id":"78efb92ec2ff4cd0b18a9cfd81a96659", "categoryId":"8", "quantity":"1", "sole_price":"199", "product_name":" Three Secondz 3 LED Touch Self Stick and Click Push OnOff Light, Battery Operated (Silver) ", "status":"pending", "orderDate":"2023-12-07 12:09:51.373", "underReturnPolicy":"0", "invoiceDetails":{"invoiceId":10," orderId":"OD-082a8efcb1954ffb8bf4c0eb8abe91ec", "taxType":"IGST" ,"orderDetails":{"orderId":"OD-082a8efcb1954ffb8bf4c0eb8abe91ec", "fname":"rohit", "lname":"k", "shippingAddressPrimary":"test", "state":"Manipur", "landmark":"null", "city":"nashik", "postalCode":"123321", "orderDate":"2023-12-07 12:09:51.373", "contactNumber":"9702990419", "paymentStatus":"success", "orderStatus":"active", "razorpayOrderDetails":null, "token":"null", "totalBill":"697.0", "paymentDetails":null,"user":{"userId":"8", "firstName":"rohit", "lastName":"k", "phone":"+919702990419", "email":"rohit@gmail.com", "shippingAddress":"com.marketplace.api.entitys.ShippingAddress@1cef1a7", "billingAddress":"com.marketplace.api.entitys.BillingAddress@39a3ac3e" , "role":"ROLE_NORMAL"},"products":"[]"}} ,"sellerId":"nuviosellers@gmail.com"}]
//				System.err.println(invoice.getInvoiceId()+" :"+invoice.getItems());
				map.put("invoiceId",invoice.getInvoiceId());
				map.put("taxType",invoice.getTaxType());
				map.put("invoice",invoice.getItems());
				map.put("invoiceStatus",invoice.getOrderStatus());
				saveDetailsInManageE(s.getApiEndpoint() + "/saveOrder", map);
//				ObjectMapper mapper = new ObjectMapper();
//				String json = null;
//				try {
//					json = mapper.writeValueAsString(map);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//				//System.out.println(json);
//				okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
//				logger.info("ADD SELLER ORDER >> EMAIL : "+s.getEmailAddr()+" >> "+s.getApiEndpoint());
//				
//				Request request = new Request.Builder().url(s.getApiEndpoint() + "/saveOrder").post(body)
//						.addHeader("accept", "application/json").addHeader("Content-Type", "application/json")
//						.build();
//
//				response = client.newCall(request).execute();
//				response.close();
			}
			
			

//			for (Invoice in : orderDetails.getInvoices()) {
//				for (OrderProducts op : in.getItems()) {
//					Seller s = sellerRepository.findByEmailAddr(op.getSellerId());
//					map.put("order", op);
//					ObjectMapper mapper = new ObjectMapper();
//					String json = null;
//					try {
//						json = mapper.writeValueAsString(map);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					System.out.println("**********************************************");
//					System.out.println(json);
//
//					okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
//					Request request = new Request.Builder().url(s.getApiEndpoint() + "/saveOrder").post(body)
//							.addHeader("accept", "application/json").addHeader("Content-Type", "application/json")
//							.build();
//
//					response = client.newCall(request).execute();
//					response.close();
//				}
//			}
			logger.info("("+orderDetails.getOrderId()+") order detail saved in manageEcom.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error : "+e.getMessage());
			return false;
		}finally {
			//response.close();
			logger.info("manageEcom action done.");
		}
	}
	
	private int saveDetailsInManageE(String API_URL,Map<String,Object> map) {
		Response response=null;
		try {
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			ObjectMapper mapper = new ObjectMapper();
			String json = null;
			try {
				json = mapper.writeValueAsString(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			logger.info("### TRACE ### [OrderDetailsService.saveDetailsInManageE] Calling ManageEcom");
			logger.info("### TRACE ### URL: {}", API_URL);
			logger.info("### TRACE ### Method: POST");
			logger.info("### TRACE ### Payload: {}", json);

			okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
			logger.info("ADD SELLER ORDER >> EMAIL : "+API_URL);
			
			Request request = new Request.Builder().url(API_URL).post(body)
					.addHeader("accept", "application/json").addHeader("Content-Type", "application/json")
					.build();

			response = client.newCall(request).execute();
			logger.info("### TRACE ### [OrderDetailsService.saveDetailsInManageE] Status: {}", response.code());
			return 0;
		}catch(Exception e) {
			logger.error("### TRACE ### [OrderDetailsService.saveDetailsInManageE] Error: {}", e.getMessage());
			e.printStackTrace();
			return -1;
		}finally {
			if (response != null) response.close();
			logger.info("api response closed.");
		}
	};
	
	private void createSettelment(OrderDetails orderDetails) {		
		try {
		    // Using a Set to handle duplicate invoices
		    Set<Integer> processedInvoices = new HashSet<>();
		    ObjectMapper objectMapper=new ObjectMapper();


		    for (Invoice invoice : orderDetails.getInvoices()) {
		        // Skip already processed invoices
		        if (processedInvoices.contains(invoice.getInvoiceId())) {
		            continue;
		        } else {
		            processedInvoices.add(invoice.getInvoiceId());
		        }

		        // Loop through each product in the invoice
		        for (OrderProducts product : invoice.getItems()) {
		        	
		            Settlement settlement = new Settlement();
		            settlement.setCreatedDtTime(new Date());
		            settlement.setItemQuantity(product.getQuantity());
		            float itemPrice = product.getQuantity() * product.getSole_price();
		            settlement.setSellerId(product.getSellerId());
		            settlement.setProductId(product.getId());
		            settlement.setItemPrice(itemPrice);
		            float marketplaceCharge = (float) (MARKETPLACE_CHARGE / 100.0) * itemPrice;
		            settlement.setMarketplaceCharge(marketplaceCharge);
		            float serviceCharge = (float) (SERVICE_CHARGE / 100.0) * itemPrice;
		            settlement.setServiceCharge(serviceCharge);
		            settlement.setFinalAmt(itemPrice - (marketplaceCharge + serviceCharge));
		            settlement.setOrderProduct(product);
		            
		            product.setSettlement(settlement);

		             //Detach product from session to avoid session conflicts (Optional)
		            // entityManager.detach(product);

		           // System.out.println(settlement.toString());
		            int settlementId = settlementRepository.save(settlement).getSettlementId();
		            Seller s = sellerRepository.findByRegisteredSellerId(product.getSellerId());
		            Map<String,Object>map=new HashMap<>();
		            MarketplaceAdmin ma = marketplaceAdminSetting.getSettings();
		            map.put("settlement",objectMapper.convertValue(settlement, Map.class));
		            map.put("settlementId", settlementId);
		            map.put("marketplaceEmail", ma.getEmail());
					map.put("marketplaceName", ma.getCompanyName());
		            saveDetailsInManageE(s.getApiEndpoint()+"/saveSettlement", map);
		            logger.info("Settlement saved.");
		        }
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}


	public ResponseEntity<?> updateOrderproductStatus(String orderProductId, String status) {
		try {
			OrderProducts orderProduct = orderProductRepository.findByOrderProductId(orderProductId);
			if (orderProduct != null) {
				String oldStatus = orderProduct.getStatus();
				if (oldStatus == null || !oldStatus.equalsIgnoreCase(status)) {
					orderProduct.setStatus(status);
					orderProductRepository.save(orderProduct);

					try {
						emailAction.sendOrderStatusUpdateEmail(orderProduct, oldStatus, status);
					} catch (Exception ex) {
						logger.error("Failed to send order status update email for OrderProduct ID {}: {}", orderProductId, ex.getMessage());
					}
				}
			}
			return ResponseEntity.ok(Map.of("status", 0, "message", "status changed!!"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", -1, "message", e.getMessage()));
		}
	}
	
	public Boolean saveReferralOrderDetails(OrderDetails order) {
		try {
			Map<String,Object> marketplaceSetupInfo=marketplaceAdminSetting.getMarketplaceSetupInfo();
			int commissionRate= (int) marketplaceSetupInfo.get("affiliateCommissionRate");
			if(commissionRate==0) {
				logger.warn("AFFILIATE COMMISION RATE IS ZERO !!");
				logger.info("AFFILIATE ORDER DETAILS : buyerId : "+order.getUser().getUserId()+" | affiliate_marketer : "+order.getUser().getReportingAccountId()+" | order_id : "+order.getOrderId());
			}
			ReferralOrders refOrder = new ReferralOrders(order.getUser().getReportingAccountId(),order.getUser().getUserId(),order.getOrderId(),commissionRate,order.getTotalBill(),"processing","N");
			referralOrdersRepository.save(refOrder);
			return true;
		}catch(Exception e) {
			logger.error("- saveReferralOrderDetails - ERROR : "+e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Create Razorpay Order for an existing marketplace order.
	 * This is called after the order is created in database but before payment.
	 * 
	 * @param marketplaceOrderId The marketplace order ID (e.g., "OD-xxxxx")
	 * @return JSONObject with Razorpay order details or error
	 */
	public JSONObject createRazorpayOrderRequest(String marketplaceOrderId) {
		try {
			logger.info("Creating Razorpay order for marketplace order: " + marketplaceOrderId);

			OrderDetails order = orderDetailsRepository.findByOrderId(marketplaceOrderId);
			if (order == null) {
				logger.error("Marketplace order not found: " + marketplaceOrderId);
				return new JSONObject().put("status", -1).put("message", "Order not found");
			}

			// Razorpay expects amount in paise (1 rupee = 100 paise)
			long amountInPaise = (long) (order.getTotalBill() * 100);
			String userEmail = order.getUser().getEmail();

			// Create Razorpay order
			Order razorpayOrder = razorpayService.createOrder(amountInPaise, marketplaceOrderId, userEmail);

			String razorpayOrderId = razorpayOrder.get("id").toString();
			logger.info("Razorpay order created: " + razorpayOrderId + " for marketplace order: " + marketplaceOrderId);

			// Store Razorpay order ID in PaymentDetails
			PaymentDetails paymentDetails = new PaymentDetails();
			paymentDetails.setRzOrderId(razorpayOrderId);
			paymentDetails.setOrderDetails(order);
			paymentDetails.setDate(new Date());
			paymentDetailsRepository.save(paymentDetails);

			// Store Razorpay order details in order
			order.setRazorpayOrderDetails(razorpayOrder.toString());
			orderDetailsRepository.save(order);

			JSONObject response = new JSONObject();
			response.put("status", 0);
			response.put("message", "Razorpay order created successfully");
			response.put("razorpayOrderId", razorpayOrderId);
			response.put("razorpayKeyId", razorpayService.getActiveKeyId());
			response.put("amount", order.getTotalBill());
			response.put("currency", "INR");
			response.put("userEmail", userEmail);

			return response;

		} catch (Exception e) {
			logger.error("Error creating Razorpay order for: " + marketplaceOrderId + " - " + e.getMessage());
			e.printStackTrace();
			return new JSONObject().put("status", -1).put("message", "Failed to create Razorpay order");
		}
	}

	/**
	 * Verify Razorpay payment signature server-side.
	 * This is the critical security verification after user completes payment.
	 * 
	 * @param razorpayOrderId Razorpay Order ID
	 * @param razorpayPaymentId Razorpay Payment ID
	 * @param razorpaySignature Razorpay Signature (from frontend)
	 * @param email User email
	 * @return JSONObject with status and result
	 */
	public JSONObject verifyRazorpayPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,
			String email) {
		try {
			logger.info("Verifying Razorpay payment - OrderId: " + razorpayOrderId + ", PaymentId: " + razorpayPaymentId);

			// Find order by Razorpay Order ID
			PaymentDetails paymentDetails = paymentDetailsRepository.findByRzOrderId(razorpayOrderId);
			if (paymentDetails == null) {
				logger.error("Payment details not found for Razorpay order: " + razorpayOrderId);
				return new JSONObject().put("status", -1).put("message", "Order not found");
			}

			OrderDetails order = paymentDetails.getOrderDetails();

			// Duplicate payment protection - check if already processed
			if ("success".equalsIgnoreCase(order.getPaymentStatus())) {
				logger.warn("Duplicate payment attempt for order: " + order.getOrderId());
				return new JSONObject().put("status", 1).put("message", "Payment already processed");
			}

			// Verify signature server-side
			boolean isSignatureValid = razorpayService.verifyPaymentSignature(razorpayOrderId, razorpayPaymentId,
					razorpaySignature);

			if (!isSignatureValid) {
				logger.error("Payment signature verification failed for order: " + order.getOrderId());
				order.setPaymentStatus("failed");
				order.setPaymentGatewayStatus("Signature verification failed");
				orderDetailsRepository.save(order);
				return new JSONObject().put("status", -1).put("message", "Payment verification failed");
			}

			logger.info("Payment signature verified for order: " + order.getOrderId());

			// Update payment details
			paymentDetails.setRzPaymentId(razorpayPaymentId);
			paymentDetails.setRzSignature(razorpaySignature);
			paymentDetailsRepository.save(paymentDetails);

			// Mark payment as successful
			order.setPaymentStatus("success");
			order.setPaymentGatewayStatus("Razorpay payment verified successfully");
			orderDetailsRepository.save(order);

			// Process the successful payment
			processSuccessfulPayment(order, email);

			JSONObject response = new JSONObject();
			response.put("status", 0);
			response.put("message", "Payment verified successfully");
			response.put("orderId", order.getOrderId());
			return response;

		} catch (Exception e) {
			logger.error("Error verifying Razorpay payment: " + e.getMessage());
			e.printStackTrace();
			return new JSONObject().put("status", -1).put("message", "Internal server error");
		}
	}

	/**
	 * Process successful payment - common logic after payment verification.
	 * Updates order status, sends emails, creates settlement, etc.
	 * 
	 * @param order The order details
	 * @param email User email
	 */
	private void processSuccessfulPayment(OrderDetails order, String email) {
		try {
			logger.info("Processing successful payment for order: " + order.getOrderId());

			// Save order into ManageEcom system
			saveOrderIntoManageEcom(order);

			// Create settlement for sellers
			createSettelment(order);

			// Save referral/affiliate order details
			saveReferralOrderDetails(order);

			// Send confirmation email
			if (IS_PRODUCTION) {
				emailAction.sendOrderSuccessEmail(email, order);
			}

			// Mark coupon as used if applicable
			if (order.getCouponCode() != null) {
				CouponUsers couponUser = new CouponUsers();
				couponUser.setCouponCode(order.getCouponCode());
				couponUser.setUserId(email);
				couponUsersRepository.save(couponUser);
			}

			logger.info("Payment processing completed for order: " + order.getOrderId());

		} catch (Exception e) {
			logger.error("Error processing successful payment for order: " + order.getOrderId() + " - " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Handle Razorpay webhook events.
	 * Verifies webhook authenticity and processes relevant payment events.
	 * 
	 * @param webhookBody Raw webhook body
	 * @param webhookSignature Webhook signature from X-Razorpay-Signature header
	 * @return JSONObject with processing result
	 */
	public JSONObject handleRazorpayWebhook(String webhookBody, String webhookSignature) {
		try {
			logger.info("Processing Razorpay webhook");

			// Verify webhook signature
			if (!razorpayService.verifyWebhookSignature(webhookBody, webhookSignature)) {
				logger.error("Webhook signature verification failed");
				return new JSONObject().put("status", -1).put("message", "Signature verification failed");
			}

			JSONObject webhookEvent = new JSONObject(webhookBody);
			String eventType = webhookEvent.getString("event");

			logger.info("Webhook event type: " + eventType);

			// Handle payment.authorized or payment.captured events
			if ("payment.authorized".equals(eventType) || "payment.captured".equals(eventType)) {
				JSONObject payload = webhookEvent.getJSONObject("payload");
				JSONObject payment = payload.getJSONObject("payment");

				String paymentId = payment.getString("id");
				String orderId = payment.getString("order_id");

				logger.info("Webhook: Processing payment " + paymentId + " for order " + orderId);

				// Find and update order
				PaymentDetails paymentDetails = paymentDetailsRepository.findByRzOrderId(orderId);
				if (paymentDetails != null) {
					OrderDetails order = paymentDetails.getOrderDetails();

					// Prevent duplicate processing
					if (!"success".equalsIgnoreCase(order.getPaymentStatus())) {
						paymentDetails.setRzPaymentId(paymentId);
						paymentDetails.setDate(new Date());
						paymentDetailsRepository.save(paymentDetails);

						order.setPaymentStatus("success");
						order.setPaymentGatewayStatus("Razorpay webhook: " + eventType);
						orderDetailsRepository.save(order);

						// Process successful payment
						processSuccessfulPayment(order, order.getUser().getEmail());

						logger.info("Webhook: Order marked as paid - " + order.getOrderId());
						return new JSONObject().put("status", 0).put("message", "Webhook processed successfully");
					} else {
						logger.warn("Webhook: Duplicate event for order " + orderId);
						return new JSONObject().put("status", 0).put("message", "Event already processed");
					}
				} else {
					logger.warn("Webhook: Order not found - " + orderId);
					return new JSONObject().put("status", -1).put("message", "Order not found");
				}
			}

			// Other webhook events are logged but not processed
			logger.info("Webhook event type not processed: " + eventType);
			return new JSONObject().put("status", 0).put("message", "Event logged");

		} catch (Exception e) {
			logger.error("Error processing Razorpay webhook: " + e.getMessage());
			e.printStackTrace();
			return new JSONObject().put("status", -1).put("message", "Internal server error");
		}
	}
	
}
