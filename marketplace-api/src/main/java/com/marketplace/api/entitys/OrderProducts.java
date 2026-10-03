package com.marketplace.api.entitys;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;


import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
public class OrderProducts {
	@Id 
	private String orderProductId;
	
	@Column(name="product_ref_id")
	private String id;
	
	private int categoryId;
	
	private int quantity;
	
	private int sole_price;
	
	private String product_name;
	
	private String status;
	
	private Date orderDate;
	
	private int sellerId;
	
	private String couponCode;
	
	String taxRate;
	float taxAmt;

	private String underReturnPolicy;
	
	String sku;
	
	int warehouseId;
	
	/*
	@ManyToOne
	@JsonBackReference
	private OrderDetails orderDetails;*/

	@ManyToOne
	@JsonBackReference
	private Invoice invoiceDetails;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "settlement")
	private Settlement settlement;
	

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
/*
	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}*/

	

	public String getOrderProductId() {
		return orderProductId;
	}

	public void setOrderProductId(String orderProductId) {
		this.orderProductId = orderProductId;
	}

	public Invoice getInvoiceDetails() {
		return invoiceDetails;
	}

	public void setInvoiceDetails(Invoice invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}

	public int getSole_price() {
		return sole_price;
	}

	public void setSole_price(int sole_price) {
		this.sole_price = sole_price;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getUnderReturnPolicy() {
		return underReturnPolicy;
	}

	public void setUnderReturnPolicy(String underReturnPolicy) {
		this.underReturnPolicy = underReturnPolicy;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public float getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(float taxAmt) {
		this.taxAmt = taxAmt;
	}
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	public void setSettlement(Settlement settlement) {
		this.settlement = settlement;
	}

	@Override
	public String toString() {
		return "{\"order_product_id\":\"" + orderProductId + "\", \"product_id\":\"" + id + "\", \"categoryId\":\"" + categoryId
				+ "\", \"quantity\":\"" + quantity + "\", \"sole_price\":\"" + sole_price + "\", \"product_name\":\"" + product_name
				+ "\", \"status\":\"" + status + "\", \"orderDate\":\"" + orderDate + "\", \"underReturnPolicy\":\"" + underReturnPolicy
				+ "\", \"invoiceDetails\":"+invoiceDetails+" ,\"sellerId\":\""+sellerId+"\", \"warehouseId\":"+warehouseId+"}";
	}

	
	
}
