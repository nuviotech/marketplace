package com.marketplace.api.entitys;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Policys {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int policyId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDtTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date updatedDtTime;
	
	@Lob
	private String policy;
	
	private String policyType;

	public int getPolicyId() {
		return policyId;
	}

	public void setPolicyId(int policyId) {
		this.policyId = policyId;
	}

	public Date getCreatedDtTime() {
		return createdDtTime;
	}

	public void setCreatedDtTime(Date createdDtTime) {
		this.createdDtTime = createdDtTime;
	}

	public Date getUpdatedDtTime() {
		return updatedDtTime;
	}

	public void setUpdatedDtTime(Date updatedDtTime) {
		this.updatedDtTime = updatedDtTime;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	
	
}
