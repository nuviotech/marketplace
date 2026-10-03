package com.marketplace.api.serviceClasses;

import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.marketplace.api.drools.CouponPojo;
import com.marketplace.api.drools.CouponValidation;
import com.marketplace.api.entitys.Coupon;
import com.marketplace.api.repository.CouponRepository;
import com.marketplace.api.repository.CouponUsersRepository;
import com.marketplace.api.util.CacheHandler;
import com.marketplace.api.util.ProductApis;

@Service
public class CouponService {
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	CouponUsersRepository couponUsersRepository;
	@Autowired
	KieContainer kieContainer;
	@Autowired
	CacheHandler cacheHandler;
	
	@Autowired
	ProductApis productApis;
	
	Logger logger = LoggerFactory.getLogger(CouponService.class);
	public ResponseEntity<?> validateCoupon(JSONArray jsonArray,String couponCode,int userOrderTotal){
		try {
			Date date=new Date();
			Coupon coupon = couponRepository.findByCouponCode(couponCode);
			int totalCouponUsageCount= couponUsersRepository.countByCouponCode(couponCode);
			
			if(coupon!=null) {
				CouponPojo couponReq=new CouponPojo();
				couponReq.setDate(date);
				couponReq.setoCouponCode(coupon.getCouponCode());
				couponReq.setdCouponCode(couponCode);
				couponReq.setEndDate(coupon.getEndDate());
				couponReq.setStartDate(coupon.getStartDate());
				couponReq.setMinimumPurchaseAmt(coupon.getMinimumPurchasetAmt());
				couponReq.setTotalOrderAmt(userOrderTotal);
				couponReq.setLimitUser(coupon.getFirstLimitedUser());
				couponReq.setTotalCouponUserCount(totalCouponUsageCount);
				couponReq.setStatus(coupon.getStatus());
				
				CouponValidation couponValidation = new CouponValidation();
				
				KieSession kieSession = kieContainer.newKieSession();
		        kieSession.setGlobal("couponValidationResponse", couponValidation);
		        kieSession.insert(couponReq);
		        kieSession.fireAllRules();
		        kieSession.dispose();
		        
		        if(couponValidation.getErrorMessage()!=null) {
		        	return ResponseEntity.status(HttpStatus.OK).body(Map.of("status",1,"message",couponValidation.getErrorMessage()));
		        }else if(couponValidation.isMinimumPurchaseAmtGreterOrEqual()) {
		        	return ResponseEntity.status(HttpStatus.OK).body(Map.of("status",1,"message","Make a order of rs "+coupon.getMinimumPurchasetAmt()+" to activate this coupon code."));
		        }
		        
		        
		        String[] array= getDiscountedAmount(jsonArray,coupon.getDiscountAmount(),"all");
		        int discountedPrice =(int)Float.parseFloat(array[0]);
		        String discountedProductIds= array[1];
		        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status",0,"message","Coupon is successfully applied.","discountedPrice",discountedPrice, "discountedProductIds",discountedProductIds,"couponDiscount",coupon.getDiscountAmount()));

			}else
				return ResponseEntity.status(HttpStatus.OK).body(Map.of("status",1,"message","wrong coupon code!!"));
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status",-1,"message",e.getMessage()));
			
		}
	}
	
	
	private String[] getDiscountedAmount(JSONArray jsonArray,float discount, String discountCategory) {
		try {
			String array[]=new String[2];
			
			StringBuilder listOfCouponApplicableProduct=new StringBuilder("");
			if (jsonArray != null) {
				float totalAmt=0f;
				for (int i = 0; i < jsonArray.length(); i++) {
					
					//jsnobject => {"quantity":1,"id":"e23htjdj"}
					JSONObject jsnobject = new JSONObject(jsonArray.get(i).toString());
					//productString => {"imgUrl2":null,"color":"Any","keywords":null,"imgUrl3":null,"imgUrl1":null,"companyName":"bca","imgUrl6":null,"imgUrl7":null,"description":"Sandwich maker","imgUrl4":null,"imgUrl5":null,"title":"sandwich maker","imgUrl8":null,"bulletpoints":null,"price":199,"id":"e23htjdj","brand":"nw","slug":null,"stockDetails":" {\"stockId\" : 177590, \"sku\":\"Gujia_maker\",\"shelfCode\":\"55\", \"quantity\":100, \"itemLength\":\"20\", \"itemHeight\":\"30\", \"itemWidth\":\"20\", \"itemWeight\":\"40\", \"inventoryType\":0,\"warehouse_id\":\"0\"}","images":[{"url":"https://manageecom.com/img/e23htjdj/null"}],"imgMain":null,"quantity":0,"is_active":true,"hsnCode":"3306","productRefId":"e23htjdj","mrp":199,"sale_price":99,"userId":"rnk0775@gmail.com","stock_id":177590,"cud":"U","createdDtTime":null,"material":"plastic","hsnRate":"18","categoryId":8}
										
					JSONObject prouctJson = new JSONObject(productApis.getProductById(jsnobject.getString("id")));
					
					if(discountCategory.equals("all")) {
						//all product
						listOfCouponApplicableProduct.append(prouctJson.getString("id")+" ");
						totalAmt+=jsnobject.getInt("quantity")*calculateCouponDiscountPrice(prouctJson.getFloat("sale_price"),discount);
					}else if (discountCategory.equals(prouctJson.getString("categoryId"))){
						//categoriwise
						listOfCouponApplicableProduct.append(prouctJson.getString("id")+" ");
						totalAmt+=jsnobject.getInt("quantity")*calculateCouponDiscountPrice(prouctJson.getFloat("sale_price"),discount);
					}else
						totalAmt+=jsnobject.getInt("quantity")*prouctJson.getFloat("sale_price");
				}
				array[0]=totalAmt+"";
				array[1]=listOfCouponApplicableProduct.toString();
				return array;
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public float calculateCouponDiscountPrice(float productPrice,float discount) {
		return (productPrice - ((discount*productPrice)/100));
	}
	
	public ResponseEntity<?> getDefaultCouponCode(){
		try {
			Coupon coupon= couponRepository.findByIsDefault("yes");
			if(coupon!=null) {
				return ResponseEntity.ok(Map.of("coupon",coupon.getCouponCode(),"status",0));
			}else {
				return ResponseEntity.ok(Map.of("status",1));
			}
		}catch(Exception e) {
			logger.error("DEFAULT_COUPON_ERROR : "+e.getMessage());
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("status",-1,"message",e.getMessage()));
		}
	}
	
}
