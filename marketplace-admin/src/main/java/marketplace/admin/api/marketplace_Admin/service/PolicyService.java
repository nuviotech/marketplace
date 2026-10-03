package marketplace.admin.api.marketplace_Admin.service;

import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.Policys;

public interface PolicyService {
	public String getEditPolicyTemplate(String type,Model model);

	public String savePolicyDetails(Policys policy, Model model);
}
