package marketplace.admin.api.marketplace_Admin.service;

import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.ReportingAccount;

public interface ReportingAccountService {
	public String getReportingAccountDetails(int page,Model model);
	public String saveReportingAccountDetails(ReportingAccount reportingAccount,Model model);
	public String getReportingAccountForm(int reportingAccntId,Model model);
	public String getReferralOrders(int reportingAccntId,int page,Model model);
	public String referralOrderSetAsDistributed(int referralId);
}
