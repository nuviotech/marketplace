package marketplace.admin.api.marketplace_Admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import marketplace.admin.api.marketplace_Admin.enititys.ReportingAccount;

public interface ReportingAccountRepo extends JpaRepository<ReportingAccount , Integer> {
	ReportingAccount findByReportingAccountId(int reportingAccntId);
}
