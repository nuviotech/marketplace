package marketplace.admin.api.marketplace_Admin.service;

import org.springframework.ui.Model;

public interface CategoriesCommissionService {
	public String getCategoriesCommission(Model model);
	public String editcategoriesCommission(int commission,String catId ); 
}
