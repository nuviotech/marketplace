package marketplace.admin.api.marketplace_Admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import marketplace.admin.api.marketplace_Admin.enititys.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	public Coupon findByCouponId(int couponId);
	public Coupon findByCouponCode(String couponCode);
	public Coupon findByIsDefault(String isDefault);
}
