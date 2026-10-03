package com.marketplace.api.entitys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

//user are those people register on nuvio.in (register persons)
@Entity
public class User implements LoginUser {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	int userId;
	
	String firstName;
	String lastName;
	
	String phone;
	
	@Column(name="Email" ,unique = true)
	String email;
	
	String password;
	
	@Column(unique = true, nullable = true)
	String googleId;
	
	@Transient
	String loginGateway; //like login using google,facebook or custom login
	
	@Column(columnDefinition = "INT DEFAULT 0")
	int reportingAccountId;
	
//	String city;
//	String shippingAddress;
//	String billingAddress;
//	String country="india";
//	String state;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shipping_address_id")
	@JsonManagedReference
	private ShippingAddress shippingAddress;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "billing_address_id")
	@JsonManagedReference
	private BillingAddress billingAddress;
	
	
	String resetPasswordVerificationId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	@JsonManagedReference
	List<OrderDetails> orders = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	@JsonManagedReference
	List<RecentlyViewedProducts> recentlyViewedProducts = new ArrayList<>();
	
	//the role is specify for login user is admin user or normal user
	String role;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	Date createdDtTime;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
//
//	public String getCity() {
//		return city;
//	}
//
//	public void setCity(String city) {
//		this.city = city;
//	}
//
//	public String getShippingAddress() {
//		return shippingAddress;
//	}
//
//	public void setShippingAddress(String shippingAddress) {
//		this.shippingAddress = shippingAddress;
//	}
//
//	public String getBillingAddress() {
//		return billingAddress;
//	}
//
//	public void setBillingAddress(String billingAddress) {
//		this.billingAddress = billingAddress;
//	}
//
//	public String getCountry() {
//		return country;
//	}
//
//	public void setCountry(String country) {
//		this.country = country;
//	}

	public List<OrderDetails> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDetails> orders) {
		this.orders = orders;
	}

	public List<RecentlyViewedProducts> getRecentlyViewedProducts() {
		return recentlyViewedProducts;
	}

	public void setRecentlyViewedProducts(List<RecentlyViewedProducts> recentlyViewedProducts) {
		this.recentlyViewedProducts = recentlyViewedProducts;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLoginGateway() {
		return loginGateway;
	}

	public void setLoginGateway(String loginGateway) {
		this.loginGateway = loginGateway;
	}
	
	public String getResetPasswordVerificationId() {
		return resetPasswordVerificationId;
	}

	public void setResetPasswordVerificationId(String resetPasswordVerificationId) {
		this.resetPasswordVerificationId = resetPasswordVerificationId;
	}
	
	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	

//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}

	public ShippingAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(ShippingAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public BillingAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}
	
	public int getReportingAccountId() {
		return reportingAccountId;
	}

	public void setReportingAccountId(int reportingAccountId) {
		this.reportingAccountId = reportingAccountId;
	}

	@Override
	public String toString() {
		return "{\"userId\":\"" + userId + "\", \"firstName\":\"" + firstName + "\", \"lastName\":\"" + lastName + "\", \"phone\":\"" + phone
				+ "\", \"email\":\"" + email + "\", \"shippingAddress\":\"" + shippingAddress + "\", \"billingAddress\":\"" + billingAddress + "\" , \"role\":\"" + role + "\"}";
	}

}
