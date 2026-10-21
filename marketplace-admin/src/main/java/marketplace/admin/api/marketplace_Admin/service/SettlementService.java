package marketplace.admin.api.marketplace_Admin.service;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

public interface SettlementService {
	public String getSettlementDetails(int page,String startDate,String endDate,Model model);
	public void downloadExcelFile(String startDate, String endDate,HttpServletResponse response);
}
