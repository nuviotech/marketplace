package com.marketplace.api.entitys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class OrderDetails {
	@Id
	private String orderId;
	
	private String fname;
	private String lname;
	private String shippingAddressPrimary;
	private String state;
	private String landmark;
	private String city;
	private String postalCode;
	private Date orderDate;
	private String contactNumber;
	private String paymentStatus;
	private String paymentType;//cod or online
	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer codPrepaidAmountPercentage;
	private String codPrepaidPaymentStatus;
	private String orderStatus;
	private String taxType;
	@Column(length = 1000)
	private String paymentGatewayStatus;
	private String gstin;
	private Integer shippingFee;
	@Column(columnDefinition = "INT DEFAULT 1")
	private Integer totalProducts;


	//here store the razorpay order datetails
	private String razorpayOrderDetails;
	//this token helps to identify the user is valid or not or get the user details
	@Transient
	private String token;

	private String couponCode;
	
	@Transient
	private float orderTotalAmtBeforeApplyingCoupon;
	
	private float totalBill;
	
	/*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "orderDetails")
	//@JsonIgnore
	@JsonManagedReference*/
	@Transient
	private List<OrderProducts> products=new ArrayList<>();
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "invoiceOrderId")
	//@JsonIgnore
	@JsonManagedReference
	private List<Invoice> invoices=new ArrayList<>();
	
	@ManyToOne
	@JsonBackReference
	private User user;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_details_id")
	@JsonManagedReference
	private PaymentDetails paymentDetails;
	
	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public List<OrderProducts> getProducts() {
		return products;
	}

	public void setProducts(List<OrderProducts> products) {
		this.products = products;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getShippingAddressPrimary() {
		return shippingAddressPrimary;
	}

	public void setShippingAddressPrimary(String shippingAddressPrimary) {
		this.shippingAddressPrimary = shippingAddressPrimary;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public PaymentDetails getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public float getTotalBill() {
		return totalBill;
	}

	public void setTotalBill(float totalBill) {
		this.totalBill = totalBill;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRazorpayOrderDetails() {
		return razorpayOrderDetails;
	}

	public void setRazorpayOrderDetails(String razorpayOrderDetails) {
		this.razorpayOrderDetails = razorpayOrderDetails;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public String getPaymentGatewayStatus() {
		return paymentGatewayStatus;
	}

	public void setPaymentGatewayStatus(String paymentGatewayStatus) {
		this.paymentGatewayStatus = paymentGatewayStatus;
	}
	

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	public float getOrderTotalAmtBeforeApplyingCoupon() {
		return orderTotalAmtBeforeApplyingCoupon;
	}

	public void setOrderTotalAmtBeforeApplyingCoupon(float orderTotalAmtBeforeApplyingCoupon) {
		this.orderTotalAmtBeforeApplyingCoupon = orderTotalAmtBeforeApplyingCoupon;
	}
	
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}
	
	public Integer getCodPrepaidAmountPercentage() {
		return codPrepaidAmountPercentage;
	}

	public void setCodPrepaidAmountPercentage(Integer codPrepaidAmountPercentage) {
		this.codPrepaidAmountPercentage = codPrepaidAmountPercentage;
	}

	public String getCodPrepaidPaymentStatus() {
		return codPrepaidPaymentStatus;
	}

	public void setCodPrepaidPaymentStatus(String codPrepaidPaymentStatus) {
		this.codPrepaidPaymentStatus = codPrepaidPaymentStatus;
	}
	
	public Integer getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(Integer shippingFee) {
		this.shippingFee = shippingFee;
	}

	public Integer getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(Integer totalProducts) {
		this.totalProducts = totalProducts;
	}

	@Override
	public String toString() {
		return "{\"orderId\":\"" + orderId + "\", \"fname\":\"" + fname + "\", \"lname\":\"" + lname
				+ "\", \"shippingAddressPrimary\":\"" + shippingAddressPrimary + "\", \"state\":\""
				+ state + "\", \"landmark\":\"" + landmark + "\", \"city\":\"" + city + "\", \"postalCode\":\"" + postalCode
				+ "\", \"orderDate\":\"" + orderDate + "\", \"contactNumber\":\"" + contactNumber + "\", \"paymentStatus\":\"" + paymentStatus
				+ "\", \"orderStatus\":\"" + orderStatus + "\", \"razorpayOrderDetails\":" + razorpayOrderDetails + ", \"token\":\"" + token
				+ "\", \"totalBill\":\"" + totalBill  + "\", \"paymentDetails\":"
				+ paymentDetails + ",\"user\":"+user+",\"products\":\""+products+"\"}";
	}
	
}
