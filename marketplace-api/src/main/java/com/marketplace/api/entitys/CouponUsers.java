package com.marketplace.api.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CouponUsers {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int couponUserId;
	
	String userId;
	
	String couponCode;

	public int getCouponUserId() {
		return couponUserId;
	}

	public void setCouponUserId(int couponUserId) {
		this.couponUserId = couponUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	
}
