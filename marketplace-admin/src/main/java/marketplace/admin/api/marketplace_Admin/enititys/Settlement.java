package marketplace.admin.api.marketplace_Admin.enititys;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;


@Entity
public class Settlement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int settlementId;
	
	//registered email id
	int sellerId;
	
	String status;
	
	float itemPrice;
	
	int itemQuantity;
	
	String productId; 
	
	//marketplace owner 15%
	float marketplaceCharge;
	
	//owner charge 5%
	float serviceCharge;
	
	//seller  
	float finalAmt;
	
	Date createdDtTime;
	
	Date setelementDtTime;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_product")
	private OrderProducts orderProduct;
	
	@Transient
	private String sellerEmail;

	public int getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(int settlementId) {
		this.settlementId = settlementId;
	}

	
	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(float itemPrice) {
		this.itemPrice = itemPrice;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public float getMarketplaceCharge() {
		return marketplaceCharge;
	}

	public void setMarketplaceCharge(float marketplaceCharge) {
		this.marketplaceCharge = marketplaceCharge;
	}

	public float getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(float serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public float getFinalAmt() {
		return finalAmt;
	}

	public void setFinalAmt(float finalAmt) {
		this.finalAmt = finalAmt;
	}

	public Date getCreatedDtTime() {
		return createdDtTime;
	}

	public void setCreatedDtTime(Date createdDtTime) {
		this.createdDtTime = createdDtTime;
	}

	public Date getSetelementDtTime() {
		return setelementDtTime;
	}

	public void setSetelementDtTime(Date setelementDtTime) {
		this.setelementDtTime = setelementDtTime;
	}

	public OrderProducts getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(OrderProducts orderProducts) {
		this.orderProduct = orderProducts;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}
	
}
