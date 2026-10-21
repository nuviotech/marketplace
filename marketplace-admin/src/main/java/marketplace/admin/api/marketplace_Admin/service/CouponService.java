package marketplace.admin.api.marketplace_Admin.service;

import java.util.Map;

import org.springframework.ui.Model;

public interface CouponService {
	public String getCouponDetails(int page, int limit,int msg, Model model);
	public String saveCouponDetails(Map<String,Object> map,Model model);
	public String changeCouponStatus(int couponId,int page,String status,Model model);
	public String manageDefaultCoupon(String operation, String couponCode);
}
