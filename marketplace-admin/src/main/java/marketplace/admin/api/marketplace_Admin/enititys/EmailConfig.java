package marketplace.admin.api.marketplace_Admin.enititys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmailConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int emailConfigId;
	
	@Column(name="sender_name")
	private String senderName;
	
	@Column(name="sender_email")
	private String senderEmail;
	
	@Column(name="password")
	private String password;
	
	//type of email like general,selas reprentative,suport,custom
	@Column(name="type",unique = true)
	private String type;

	public int getEmailConfigId() {
		return emailConfigId;
	}

	public void setEmailConfigId(int emailConfigId) {
		this.emailConfigId = emailConfigId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	/*
	@Column(name="general_sender_name")
	String generalEmailSenderName;
	
	@Column(name="general_email")
	String generalEmail;
	
	@Column(name="general_email_password")
	String generalEmailPassword;
	
	@Column(name="sales_representative_sender_name")
	String salesRepresentativeEmailSenderName;
	
	@Column(name="sales_representative_email")
	String salesRepresentativeEmail;
	
	@Column(name="sales_representative_email_password")
	String salesRepresentativeEmailPassword;
	
	@Column(name="support_sender_name")
	String supportEmailSenderName;
	
	@Column(name="suport_email")
	String supportEmail;
	
	@Column(name="custom1_email_password")
	String supportEmailPassword;
	
	@Column(name="custom1_sender_name")
	String cutom1EmailSenderName;
	
	@Column(name="custom1_email")
	String cutom1Email;
	
	@Column(name="custom1_email_password")
	String cutom1EmailPassword;
	
	@Column(name="custom2_sender_name")
	String cutom2EmailSenderName;
	
	@Column(name="custom2_email")
	String cutom2Email;
	
	@Column(name="custom2_email_password")
	String cutom2EmailPassword;
	*/
	
	
}
