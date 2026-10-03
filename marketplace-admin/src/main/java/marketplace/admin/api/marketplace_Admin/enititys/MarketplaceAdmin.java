package marketplace.admin.api.marketplace_Admin.enititys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MarketplaceAdmin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@Column(unique = true)
	private String email;
	private String password;
	private String role;
	String name;
	
	private String rz_Key;
	private String rz_Secret;
	
	private String phonpe_merchantId;
	private String phonpe_index;
	private String phonpe_saltKey;
	private String phonpe_url;
	
	String webTitle;
	String companyName;
	String logo;
	String contact;
	@Column(columnDefinition = "VARCHAR(20) DEFAULT 'disabled'")
	String codStatus;//cash on delivery is enabled or not
	
	//using this email send the message to others
	String supportEmailId;
	String supportEmailPassword;
	
	private String resetPasswordCode;
	
	@Column(columnDefinition = "VARCHAR(20) DEFAULT 'both'")
	private String businessType; //b2b b2c both
	
	
	
	@Column(columnDefinition = "INT DEFAULT 10")
    private Integer minimumPurchaseAmt;

	@Column(name="cod_prepaid_percentage" ,columnDefinition = "INT DEFAULT 0")
	private Integer codPrepaidPercentage;

	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhonpe_url() {
		return phonpe_url;
	}
	public void setPhonpe_url(String phonpe_url) {
		this.phonpe_url = phonpe_url;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRz_Key() {
		return rz_Key;
	}
	public void setRz_Key(String rz_Key) {
		this.rz_Key = rz_Key;
	}
	public String getRz_Secret() {
		return rz_Secret;
	}
	public void setRz_Secret(String rz_Secret) {
		this.rz_Secret = rz_Secret;
	}
	public String getPhonpe_merchantId() {
		return phonpe_merchantId;
	}
	public void setPhonpe_merchantId(String phonpe_merchantId) {
		this.phonpe_merchantId = phonpe_merchantId;
	}
	public String getPhonpe_index() {
		return phonpe_index;
	}
	public void setPhonpe_index(String phonpe_index) {
		this.phonpe_index = phonpe_index;
	}
	public String getPhonpe_saltKey() {
		return phonpe_saltKey;
	}
	public void setPhonpe_saltKey(String phonpe_saltKey) {
		this.phonpe_saltKey = phonpe_saltKey;
	}
	
	
	public String getWebTitle() {
		return webTitle;
	}
	public void setWebTitle(String webTitle) {
		this.webTitle = webTitle;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public String getResetPasswordCode() {
		return resetPasswordCode;
	}
	public void setResetPasswordCode(String resetPasswordCode) {
		this.resetPasswordCode = resetPasswordCode;
	}
	
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	
	public String getSupportEmailId() {
		return supportEmailId;
	}
	public void setSupportEmailId(String supportEmailId) {
		this.supportEmailId = supportEmailId;
	}
	public String getSupportEmailPassword() {
		return supportEmailPassword;
	}
	public void setSupportEmailPassword(String supportEmailPassword) {
		this.supportEmailPassword = supportEmailPassword;
	}
	
	public String getCodStatus() {
		return codStatus;
	}
	public void setCodStatus(String codStatus) {
		this.codStatus = codStatus;
	}
	
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public Integer getMinimumPurchaseAmt() {
		return minimumPurchaseAmt;
	}
	public void setMinimumPurchaseAmt(Integer minimumPurchaseAmt) {
		this.minimumPurchaseAmt = minimumPurchaseAmt;
	}
	
	public Integer getCodPrepaidPercentage() {
		return codPrepaidPercentage;
	}
	public void setCodPrepaidPercentage(Integer codPrepaidPercentage) {
		this.codPrepaidPercentage = codPrepaidPercentage;
	}
	@Override
	public String toString() {
		return "MarketplaceAdmin [id=" + id + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", name=" + name + ", rz_Key=" + rz_Key + ", rz_Secret=" + rz_Secret + ", phonpe_merchantId="
				+ phonpe_merchantId + ", phonpe_index=" + phonpe_index + ", phonpe_saltKey=" + phonpe_saltKey + "]";
	}
}
