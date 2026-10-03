package marketplace.admin.api.marketplace_Admin.service;

import org.springframework.ui.Model;

public interface UserService {
	public String getUserDetails(int page,int limit, Model model);
	
}
