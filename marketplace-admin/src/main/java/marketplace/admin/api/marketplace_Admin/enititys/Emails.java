package marketplace.admin.api.marketplace_Admin.enititys;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Emails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int emailId;
	
	@Column(name="subject")
	private String subject;
	
	@Lob
	@Column(name="emailMsg",nullable = false)
	private String emailMsg;
	
	@Lob
	@Column(name="receiverEmails")
	private String receiverEmails;
	
	@Column(name="emailType")
	private String emailType;//all user or custom user
	
	@Column(name="totalRecipient" , columnDefinition = "INT DEFAULT 1")
	private int totalRecipient;
	
	@Column(name="senderEmailType")
	private String sendeEmailType;
	
	@Column(name="createDateAndTime")
	private Date createdDateAndTime;

	public int getEmailId() {
		return emailId;
	}

	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailMsg() {
		return emailMsg;
	}

	public void setEmailMsg(String emailMsg) {
		this.emailMsg = emailMsg;
	}

	public String getReceiverEmails() {
		return receiverEmails;
	}

	public void setReceiverEmails(String receiverEmails) {
		this.receiverEmails = receiverEmails;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getSendeEmailType() {
		return sendeEmailType;
	}

	public void setSendeEmailType(String sendeEmailType) {
		this.sendeEmailType = sendeEmailType;
	}

	public Date getCreatedDateAndTime() {
		return createdDateAndTime;
	}

	public void setCreatedDateAndTime(Date createdDateAndTime) {
		this.createdDateAndTime = createdDateAndTime;
	}

	public int getTotalRecipient() {
		return totalRecipient;
	}

	public void setTotalRecipient(int totalRecipient) {
		this.totalRecipient = totalRecipient;
	}

	
	
}
