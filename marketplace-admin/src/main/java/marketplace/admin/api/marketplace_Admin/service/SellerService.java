package marketplace.admin.api.marketplace_Admin.service;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;

public interface SellerService {
	public String getAllSellers(int page, String searchKey, String searchValue ,Model model);
	public String getActiveSeller(Model model,String status, int page, int limit);
	public String activeTheSeller(String id,Model model,Principal p,MarketplaceAdmin user);
	public String deactivateTheSeller(String id,Principal p,Model model);
	public String getSellerProducts(String sellerId, int totalProductCount,int page, int limit ,Model model);
	public String editSellerMOV(String sellerEmail, int mov);
}
