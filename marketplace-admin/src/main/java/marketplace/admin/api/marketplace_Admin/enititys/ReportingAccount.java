package marketplace.admin.api.marketplace_Admin.enititys;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class ReportingAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reporting_account_id")
	private int reportingAccountId;

	@Column(name = "reporting_account_name")
	private String reportingAccountName;

	@Column(name = "phone_no", length = 20)
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_dt_time")
	Date createdDtTime;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column(name = "updated_dt_time")
	Date updatedDtTime;

	public int getReportingAccountId() {
		return reportingAccountId;
	}

	public void setReportingAccountId(int reportingAccountId) {
		this.reportingAccountId = reportingAccountId;
	}

	public String getReportingAccountName() {
		return reportingAccountName;
	}

	public void setReportingAccountName(String reportingAccountName) {
		this.reportingAccountName = reportingAccountName;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	@Override
	public String toString() {
		return "ReportingAccount [reportingAccountId=" + reportingAccountId + ", reportingAccountName="
				+ reportingAccountName + ", phone=" + phone + ", email=" + email + ", address=" + address
				+ ", createdDtTime=" + createdDtTime + ", updatedDtTime=" + updatedDtTime + "]";
	}

	
	
}
