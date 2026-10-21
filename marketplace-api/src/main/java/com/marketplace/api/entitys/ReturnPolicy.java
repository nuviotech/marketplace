package com.marketplace.api.entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ReturnPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int returnPolicyId;
	
	private int categoryId;
	
	private int policyDays;

	public int getReturnPolicyId() {
		return returnPolicyId;
	}

	public void setReturnPolicyId(int returnPolicyId) {
		this.returnPolicyId = returnPolicyId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getPolicyDays() {
		return policyDays;
	}

	public void setPolicyDays(int policyDays) {
		this.policyDays = policyDays;
	}
	
	
}
