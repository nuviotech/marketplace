package com.marketplace.api.entitys;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;



@Entity
public class PaymentDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int paymentDetailsId;
	
	String rzPaymentId;
	String rzOrderId;
	String rzSignature;
	
	Date date;
	
	@Transient
	String orderID;//this order id helps to updating the order payment send by ui
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_details_id")
	@JsonBackReference
	private OrderDetails orderDetails;

	public int getPaymentDetailsId() {
		return paymentDetailsId;
	}

	public void setPaymentDetailsId(int paymentDetailsId) {
		this.paymentDetailsId = paymentDetailsId;
	}

	public String getRzPaymentId() {
		return rzPaymentId;
	}

	public void setRzPaymentId(String rzPaymentId) {
		this.rzPaymentId = rzPaymentId;
	}

	public String getRzOrderId() {
		return rzOrderId;
	}

	public void setRzOrderId(String rzOrderId) {
		this.rzOrderId = rzOrderId;
	}

	public String getRzSignature() {
		return rzSignature;
	}

	public void setRzSignature(String rzSignature) {
		this.rzSignature = rzSignature;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	@Override
	public String toString() {
		return "{\"paymentDetailsId\":\"" + paymentDetailsId + "\", \"rzPaymentId\":\"" + rzPaymentId + "\", \"rzOrderId\":\""
				+ rzOrderId + "\", \"rzSignature\":\"" + rzSignature + "\", \"date\":\"" + date + "\"}";
	}

	
}
