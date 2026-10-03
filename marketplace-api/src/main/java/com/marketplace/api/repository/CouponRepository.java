package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.Coupon;


public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	Coupon findByCouponCode(String couponCode);
	Coupon findByIsDefault(String isDefault);
}
