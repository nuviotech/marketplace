package marketplace.admin.api.marketplace_Admin.Implementation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.Coupon;
import marketplace.admin.api.marketplace_Admin.repository.CouponRepository;
import marketplace.admin.api.marketplace_Admin.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService {
	@Autowired
	CouponRepository couponRepository;
	
	@Override
	public String getCouponDetails(int page, int limit, int msg, Model model) {
		try {
			Pageable pageable=PageRequest.of(page, limit);
			Page<Coupon> coupons = couponRepository.findAll(pageable);
			model.addAttribute("coupons", coupons);
			model.addAttribute("currentPage", page);
			
			model.addAttribute("alert", msg==0? "success":"none");
			model.addAttribute("totalPage",coupons.getTotalPages());
			return "UserPages/couponTable";
		}catch(Exception e) {
			System.out.println("Error (update user details) : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String saveCouponDetails(Map<String, Object> map, Model model) {
		try {
			
			//System.out.println(coupon.toString());
			String sDateTest=map.get("sDate")+" 00:01:00";  
		    Date sDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDateTest); 
		    
		    String eDateTest=map.get("eDate")+" 23:59:00";  
		    Date eDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eDateTest); 
		    
		    Coupon coupon=new Coupon();
			coupon.setCouponCode(map.get("couponCode")+"");
			coupon.setMinimumPurchasetAmt(Integer.parseInt(map.get("minimumPurchasetAmt")+""));
			coupon.setFirstLimitedUser(Integer.parseInt(map.get("firstLimitedUser")+""));
			coupon.setDiscountAmount(Integer.parseInt(map.get("discountAmount")+""));
			coupon.setDiscountType(map.get("discountType")+"");
			coupon.setDescription(map.get("description")+"");
			
			coupon.setStatus("active");
			
			if(sDate.compareTo(eDate) < 0) {
		         //start date small & end date big
		    	coupon.setStartDate(sDate);
		    	coupon.setEndDate(eDate);
		    }else {
		    	model.addAttribute("coupon",coupon);
		    	model.addAttribute("alert", "invalidDate");
				return "UserPages/couponForm";
		    }
			
			coupon.setCondions(map.get("condions")+"");
			couponRepository.save(coupon);
			model.addAttribute("coupon",coupon);
			return "redirect:/marketplace/coupons?page=0&msgCode=0";
		}catch(Exception e) {
			System.out.println("Error (save coupon details) : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String changeCouponStatus(int couponId,int page,String status,Model model) {
		try {
			Coupon c=couponRepository.findByCouponId(couponId);
			if(c!=null) {
				c.setStatus(status);
				couponRepository.save(c);
			}
			return "redirect:/marketplace/coupons?page="+page;
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String manageDefaultCoupon(String operation, String couponCode) {
		try {
			Coupon c =couponRepository.findByCouponCode(couponCode);
			if(c!=null) {
				if(operation.equalsIgnoreCase("add")) {
					Coupon prevCD=couponRepository.findByIsDefault("yes");
					if(prevCD!=null) {
						prevCD.setIsDefault("no");
						couponRepository.save(prevCD);
					}
					c.setIsDefault("yes");
					couponRepository.save(c);
					return "added";
				}else if(operation.equalsIgnoreCase("remove")) {
					c.setIsDefault("no");
					couponRepository.save(c);
					return "removed";
				}
				return "invalid";
			}else {
				return "Coupon Code "+couponCode+" Is Not Present In Record !!";
			}
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
