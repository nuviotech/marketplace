package com.marketplace.api.drools;

public class CouponValidation {

	boolean minimumPurchaseAmtGreterOrEqual;
	String errorMessage=null;

	public boolean isMinimumPurchaseAmtGreterOrEqual() {
		return minimumPurchaseAmtGreterOrEqual;
	}
	public void setMinimumPurchaseAmtGreterOrEqual(boolean minimumPurchaseAmtGreterOrEqual) {
		this.minimumPurchaseAmtGreterOrEqual = minimumPurchaseAmtGreterOrEqual;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "CouponValidation [minimumPurchaseAmtGreterOrEqual=" + minimumPurchaseAmtGreterOrEqual
				+ ", errorMessage=" + errorMessage + "]";
	}
}
