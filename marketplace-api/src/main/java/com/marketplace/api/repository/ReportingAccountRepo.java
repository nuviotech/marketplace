package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.ReportingAccount;


public interface ReportingAccountRepo extends JpaRepository<ReportingAccount , Integer> {
	ReportingAccount findByReportingAccountId(int reportingAccntId);
	ReportingAccount findByEmail(String email);
}
