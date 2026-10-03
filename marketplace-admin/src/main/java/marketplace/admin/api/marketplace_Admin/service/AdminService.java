package marketplace.admin.api.marketplace_Admin.service;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;

public interface AdminService {
	public String updateTheUserDetails(MarketplaceAdmin admin,Model model,Principal p);
	public String saveAdminSetting(String action, MarketplaceAdmin admin,Model model,MultipartFile logo,MultipartFile favicon,Principal p); 
	public String viewMarketplaceStatus(Principal p,Model model);
	public String setApiEndPointInPolicyMaker(Principal p, Model model);
	public String changeCodStatus(String status, Model model, Principal p);
	public String changeBusinessType(String type,Model model,Principal p);
	public String updateMinimumPurchaseAmt(int amt,Principal p);
	public String updateCodPrepaidPercentage(int amt, Principal p);
	
}
