package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Integer> {
	
	/**
	 * Find payment details by Razorpay Order ID
	 */
	PaymentDetails findByRzOrderId(String rzOrderId);
	
	/**
	 * Find payment details by Razorpay Payment ID
	 */
	PaymentDetails findByRzPaymentId(String rzPaymentId);
}
