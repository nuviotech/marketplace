package com.marketplace.api.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class ReferralOrders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int referralOrderId;
	
	@Column(name="affiliate_marketer_id")
	Integer affiliateMarketerId;
	
	@Column(name="buyer_id")
	Integer buyerId;
	
	@Column(name="order_id")
	String orderId;
	
	@Column(name="commission_rate")
	Integer commissionRate;
	
	@Column(name="purchased_amount")
	Float purchasedAmount;
	
	@Column(name="status")
	String status;
	
	//Y|N
	@Column(name="payment_settlement",length=1)
	String paymentSettlement;
	
	public ReferralOrders(Integer affiliateMarketerId, Integer buyerId, String orderId, Integer commissionRate,
			Float purchasedAmount, String status, String paymentSettlement) {
		super();
		this.affiliateMarketerId = affiliateMarketerId;
		this.buyerId = buyerId;
		this.orderId = orderId;
		this.commissionRate = commissionRate;
		this.purchasedAmount = purchasedAmount;
		this.status = status;
		this.paymentSettlement = paymentSettlement;
	}

	public ReferralOrders() {
		//default constructor
	};
	
	public int getReferralOrderId() {
		return referralOrderId;
	}

	public void setReferralOrderId(int referralOrderId) {
		this.referralOrderId = referralOrderId;
	}

	public Integer getAffiliateMarketerId() {
		return affiliateMarketerId;
	}

	public void setAffiliateMarketerId(Integer affiliateMarketerId) {
		this.affiliateMarketerId = affiliateMarketerId;
	}

	public Integer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(Integer commissionRate) {
		this.commissionRate = commissionRate;
	}

	public Float getPurchasedAmount() {
		return purchasedAmount;
	}

	public void setPurchasedAmount(Float purchasedAmount) {
		this.purchasedAmount = purchasedAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentSettlement() {
		return paymentSettlement;
	}

	public void setPaymentSettlement(String paymentSettlement) {
		this.paymentSettlement = paymentSettlement;
	}
	
}
