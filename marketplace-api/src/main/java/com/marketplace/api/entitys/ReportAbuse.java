package com.marketplace.api.entitys;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="report_abuse")
public class ReportAbuse {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name="report_abuse_id")
	int reportAbuseId;
	
	@Column(name="name")
	String name;
	
	@Column(name="email")
	String email;
	
	@Column(name="subject")
	String subject;
	
	@Column(name="nature_of_abuse")
	String natureOfAbuse;
	
	@Column(name="incident_date_time")
	String incidentDtTime;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name="created_date_time")
	Date createDtTime;
	
	@Column(name="description",length = 2000)
	String desc;
	
	@Column(name="evidence_file_name")
	String evidenceFileName;

	@Column(name="action_requested")
	String actionRequested;
	
	
	public int getReportAbuseId() {
		return reportAbuseId;
	}



	public void setReportAbuseId(int reportAbuseId) {
		this.reportAbuseId = reportAbuseId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getNatureOfAbuse() {
		return natureOfAbuse;
	}



	public void setNatureOfAbuse(String natureOfAbuse) {
		this.natureOfAbuse = natureOfAbuse;
	}



	public String getIncidentDtTime() {
		return incidentDtTime;
	}



	public void setIncidentDtTime(String incidentDtTime) {
		this.incidentDtTime = incidentDtTime;
	}



	public Date getCreateDtTime() {
		return createDtTime;
	}



	public void setCreateDtTime(Date createDtTime) {
		this.createDtTime = createDtTime;
	}



	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
	}



	public String getEvidenceFileName() {
		return evidenceFileName;
	}



	public void setEvidenceFileName(String evidenceFileName) {
		this.evidenceFileName = evidenceFileName;
	}
	
	
	public String getActionRequested() {
		return actionRequested;
	}



	public void setActionRequested(String actionRequested) {
		this.actionRequested = actionRequested;
	}



	@Override
	public String toString() {
		return "ReportAbuse [reportAbuseId=" + reportAbuseId + ", name=" + name + ", email=" + email + ", subject="
				+ subject + ", natureOfAbuse=" + natureOfAbuse + ", incidentDtTime=" + incidentDtTime
				+ ", createDtTime=" + createDtTime + ", desc=" + desc + ", evidenceFileName=" + evidenceFileName + "]";
	}
	
	
}
