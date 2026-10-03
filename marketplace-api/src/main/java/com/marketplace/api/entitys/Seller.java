package com.marketplace.api.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Seller {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int sellerID;
	
	String firstName;
	String lastName;
	
	@Column(name="Email", unique = true)
	String emailAddr;
	
	@Column(name="registered_seller_id")
	Integer registeredSellerId;
	
	@Column(name="minimum_order_value", columnDefinition = "INT DEFAULT 10")
	Integer minimumOrderValue;
	
	@Column(name="name_of_company")
	String nameOfCompany;
	
	@Column(name="type_of_company")	
	String typeOfCompany;
	
	@Column(length=3000)
	String bussinessAddr;
	
	String pincode;
	
	String GstNo;
	
	String password;
	String componyUrl;
	String status;
	
	@Transient
	String passwordConfirm;
	@Transient
	String signUpType;
	
	String phoneNumber;
	
	String country;
	String state;
	

	String apiEndpoint;
	public int getSellerID() {
		return sellerID;
	}
	public void setSellerID(int sellerID) {
		sellerID = sellerID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	public String getNameOfCompany() {
		return nameOfCompany;
	}
	public void setNameOfCompany(String nameOfCompany) {
		this.nameOfCompany = nameOfCompany;
	}
	public String getTypeOfCompany() {
		return typeOfCompany;
	}
	public void setTypeOfCompany(String typeOfCompany) {
		this.typeOfCompany = typeOfCompany;
	}
	public String getBussinessAddr() {
		return bussinessAddr;
	}
	public void setBussinessAddr(String bussinessAddr) {
		this.bussinessAddr = bussinessAddr;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getGstNo() {
		return GstNo;
	}
	public void setGstNo(String gstNo) {
		GstNo = gstNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getComponyUrl() {
		return componyUrl;
	}
	public void setComponyUrl(String componyUrl) {
		this.componyUrl = componyUrl;
	}
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	public String getSignUpType() {
		return signUpType;
	}
	public void setSignUpType(String signUpType) {
		this.signUpType = signUpType;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getApiEndpoint() {
		return apiEndpoint;
	}
	public void setApiEndpoint(String apiEndpoint) {
		this.apiEndpoint = apiEndpoint;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRegisteredSellerId() {
		return registeredSellerId;
	}
	public void setRegisteredSellerId(int registeredSellerId) {
		this.registeredSellerId = registeredSellerId;
	}
	public Integer getMinimumOrderValue() {
		return minimumOrderValue;
	}
	public void setMinimumOrderValue(Integer minimumOrderValue) {
		this.minimumOrderValue = minimumOrderValue;
	}
	
}
