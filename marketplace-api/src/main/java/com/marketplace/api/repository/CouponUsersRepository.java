package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.CouponUsers;

public interface CouponUsersRepository extends JpaRepository<CouponUsers, Integer>{
	public int countByCouponCode(String couponCode);
	public int countByCouponCodeAndUserId(String couponCode,String userEmailId);
}
