package marketplace.admin.api.marketplace_Admin.enititys;



import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ReturnDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int returnDetailsId;
	
	int amt;
	
	String orderId;
	
	int categoryId;
	
	String paymentId;
	
	String status;
	
	String reason;
	
	String email;
	
	int userId;
	
	String productName;
	
	Date returnOrderDtTime;

	@Column(length = 2000)
	String refundRZstatus;

	public int getReturnDetailsId() {
		return returnDetailsId;
	}

	public void setReturnDetailsId(int returnDetailsId) {
		this.returnDetailsId = returnDetailsId;
	}

	public int getAmt() {
		return amt;
	}

	public void setAmt(int amt) {
		this.amt = amt;
	}
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getReturnOrderDtTime() {
		return returnOrderDtTime;
	}

	public void setReturnOrderDtTime(Date returnOrderDtTime) {
		this.returnOrderDtTime = returnOrderDtTime;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getRefundRZstatus() {
		return refundRZstatus;
	}

	public void setRefundRZstatus(String refundRZstatus) {
		this.refundRZstatus = refundRZstatus;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
