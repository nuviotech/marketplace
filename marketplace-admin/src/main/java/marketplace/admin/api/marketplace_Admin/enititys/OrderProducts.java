package marketplace.admin.api.marketplace_Admin.enititys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import org.springframework.boot.context.properties.bind.DefaultValue;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	String taxRate;
	float taxAmt;

	private String underReturnPolicy;
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

	public Settlement getSettlement() {
		return settlement;
	}

	public void setSettlement(Settlement settlement) {
		this.settlement = settlement;
	}
	
	
}
