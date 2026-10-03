package com.marketplace.api.entitys;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coupon {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="COUPON_ID")
	int couponId;
	
	@Column(name="COUPON_CODE",unique = true)
	String couponCode;
	
	@Column(name="DISCOUNT_TYPE")
	String discountType;
	
	@Column(name="DISCOUNT_AMOUNT")
	int discountAmount;
	
	@Column(name="MINIMUM_PURCHASE_AMOUNT")
	int minimumPurchasetAmt;
	
	@Column(name="LIMITED_USER")
	int firstLimitedUser;
	
	@Column(name="START_DATE")
	Date startDate;
	
	@Column(name="END_DATE")
	Date endDate;
	
	@Column(name="CONDITIONS",length=2000)
	String condions;
	
	@Column(name="DISCRIPTION",length=3000)
	String description;
	
	@Column(name="STATUS",columnDefinition = "VARCHAR(20) DEFAULT 'inactive'")
	String status;
	
	//for all user provided coupon
	@Column(name="IS_DEFAULT", columnDefinition = "VARCHAR(5) DEFAULT 'no'")
	String isDefault;

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public int getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(int discountAmount) {
		this.discountAmount = discountAmount;
	}

	public int getMinimumPurchasetAmt() {
		return minimumPurchasetAmt;
	}

	public void setMinimumPurchasetAmt(int minimumPurchasetAmt) {
		this.minimumPurchasetAmt = minimumPurchasetAmt;
	}

	public int getFirstLimitedUser() {
		return firstLimitedUser;
	}

	public void setFirstLimitedUser(int firstLimitedUser) {
		this.firstLimitedUser = firstLimitedUser;
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

	public String getCondions() {
		return condions;
	}

	public void setCondions(String condions) {
		this.condions = condions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "CouponPojo [couponId=" + couponId + ", couponCode=" + couponCode + ", discountType=" + discountType
				+ ", discountAmount=" + discountAmount + ", minimumDiscountAmt=" + minimumPurchasetAmt
				+ ", firstLimitedUser=" + firstLimitedUser + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", condions=" + condions + ", description=" + description + "]";
	}
	
	
}
