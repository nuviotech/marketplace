package marketplace.admin.api.marketplace_Admin.enititys;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;


@Entity
//this is marketplace active sellers
public class Seller {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sellerID;
	
	private String firstName;
	private String lastName;
	
	@Column(name="Email", unique = true)
	private String emailAddr;
	
	@Column(name="registered_seller_id")
	private Integer registeredSellerId;
	
	@Column(name="minimum_order_value", columnDefinition = "INT DEFAULT 10")
	private Integer minimumOrderValue;
	
	@Column(name="name_of_company")
	private String nameOfCompany;
	
	@Column(name="type_of_company")	
	private String typeOfCompany;
	
	@Column(length=3000)
	private String bussinessAddr;
	
	private String pincode;
	
	private String GstNo;
	
	private String password;
	private String componyUrl;
	
	@Transient
	String passwordConfirm;
	@Transient
	String signUpType;
	
	private String phoneNumber;
	
	private String country;
	private String state;
	private String status;
	
	private String marketplaceId;
	private String categories;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	Date createdDtTime;
	
	@Transient
	int productTotalCount;
	
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
	public String getMarketplaceId() {
		return marketplaceId;
	}
	public void setMarketplaceId(String marketplaceId) {
		this.marketplaceId = marketplaceId;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}

	public int getProductTotalCount() {
		return productTotalCount;
	}
	public void setProductTotalCount(int productTotalCount) {
		this.productTotalCount = productTotalCount;
	}
	
	public int getRegisteredSellerId() {
		return registeredSellerId;
	}
	public void setRegisteredSellerId(int registeredSellerId) {
		this.registeredSellerId = registeredSellerId;
	}
	public Date getCreatedDtTime() {
		return createdDtTime;
	}
	public void setCreatedDtTime(Date createdDtTime) {
		this.createdDtTime = createdDtTime;
	}
	
	public Integer getMinimumOrderValue() {
		return minimumOrderValue;
	}
	public void setMinimumOrderValue(Integer minimumOrderValue) {
		this.minimumOrderValue = minimumOrderValue;
	}
	@Override
	public String toString() {
		return "{\"sellerID\" : " + sellerID + ", \"firstName\":\"" + firstName + "\", \"lastName\":\"" + lastName + "\", \"emailAddr\":\""
				+ emailAddr + "\", \"nameOfCompany\":\"" + nameOfCompany + "\", \"typeOfCompany\":\"" + typeOfCompany
				+ "\", \"bussinessAddr\":\"" + bussinessAddr + "\", \"pincode\":\"" + pincode + "\", \"GstNo\":\"" + GstNo + "\", \"componyUrl\":\"" + componyUrl + "\", \"phoneNumber\":\"" + phoneNumber + "\", \"country\":\"" + country + "\", \"state\":\"" + state
				+ "\", \"status\":\"" + status + "\",\"categories\":\"" + categories
				+ "\", \"apiEndpoint\":\"" + apiEndpoint + "\", \"productCount\": "+productTotalCount+"}";
	}

}
