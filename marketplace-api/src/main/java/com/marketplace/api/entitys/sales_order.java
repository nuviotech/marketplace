/*
package com.marketplace.api.entitys;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class sales_order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int SALES_ORDER_ID;
	
	int CHANNEL_ID;
	
	@Column(length = 100)
	String CHANNEL_ORDER_ID;
	
	@Column(length =100)
	String STATUS;
	
	@Column(length = 100)
	String USER_ID;
	
	@Column(length = 10)
	String SOLD_PRICE;
	
	@Column(length=250)
	String REMARKS;
	
	int CUSTOMER_ID	;
	
	@Column(length=1)
	String CUD;
	
	Date CREATED_DT_TIME;
	
	@Column(length=100)
	String BUYER_NAME;
	
	@Column(length=20)
	String BUYER_PHONE_NUMBER;
	
	@Column(length=100)
	String RECIPIENT_NAME;
	
	@Column(length=100)
	String SHIPPING_ADDRESS1;
	
	@Column(length=100)
	String SHIPPING_ADDRESS2;
	
	@Column(length=100)
	String SHIPPING_ADDRESS3;
	
	@Column(length=100)
	String SHIP_CITY;
	
	@Column(length=50)
	String SHIP_STATE;
	
	@Column(length=11)
	String SHIP_POSTAL_CODE;
	
	@Column(length=50)
	String SHIP_COUNTRY;
	
	@Column(length=100)
	String SHIP_PHONE_NUMBER;
	
	@Column(length=100)
	String PAYMENT_METHOD;
	
	@Column(length=50)
	String COD_COLLECTIBLE_AMOUNT;
	
	@Column(length=50)
	String FULFILLED_BY;
	
	@Column(length=50)
	String RETURN_REQUEST_DATE;
	
	@Column(length=100)
	String RETURN_REQUEST_STATUS;
	
	@Column(length=50)
	String COD_COLLECTIBLE_AMOUNT;
	
	@Column(length=50)
	String COD_COLLECTIBLE_AMOUNT;
	
	@Column(length=50)
	String COD_COLLECTIBLE_AMOUNT;
	
	@Column(length=50)
	String COD_COLLECTIBLE_AMOUNT;
	
	@Column(length=50)
	String COD_COLLECTIBLE_AMOUNT;
	
	
}
*/