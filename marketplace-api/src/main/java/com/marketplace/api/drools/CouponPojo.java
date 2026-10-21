package com.marketplace.api.drools;

import java.util.Date;

public class CouponPojo {
	
	String oCouponCode;//order_coupon_code
	String dCouponCode;//discound_coupon_code
	float minimumPurchaseAmt;
	float totalOrderAmt;
	int limitUser;
	int totalCouponUserCount;
	Date startDate;
	Date endDate;
	Date date;
	String status;
	
	public String getoCouponCode() {
		return oCouponCode;
	}
	public void setoCouponCode(String oCouponCode) {
		this.oCouponCode = oCouponCode;
	}
	public String getdCouponCode() {
		return dCouponCode;
	}
	public void setdCouponCode(String dCouponCode) {
		this.dCouponCode = dCouponCode;
	}
	public float getMinimumPurchaseAmt() {
		return minimumPurchaseAmt;
	}
	public void setMinimumPurchaseAmt(float minimumPurchaseAmt) {
		this.minimumPurchaseAmt = minimumPurchaseAmt;
	}
	public float getTotalOrderAmt() {
		return totalOrderAmt;
	}
	public void setTotalOrderAmt(float totalOrderAmt) {
		this.totalOrderAmt = totalOrderAmt;
	}
	public int getLimitUser() {
		return limitUser;
	}
	public void setLimitUser(int limitUser) {
		this.limitUser = limitUser;
	}
	public int getTotalCouponUserCount() {
		return totalCouponUserCount;
	}
	public void setTotalCouponUserCount(int totalCouponUserCount) {
		this.totalCouponUserCount = totalCouponUserCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
