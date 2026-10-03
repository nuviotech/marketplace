package marketplace.admin.api.marketplace_Admin.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.Policys;
import marketplace.admin.api.marketplace_Admin.repository.PolicyRepository;
import marketplace.admin.api.marketplace_Admin.service.PolicyService;

@Service
public class PolicyServiceImpl implements PolicyService {
	@Autowired
	PolicyRepository policyRepository;
	String MPFS="marketplace_policy_for_sellers";
	String MPFW="marketplace_privacy_policy_for_website";
	String MSDPFW="marketplace_shipping_and_delivery_policy_for_website";
	String MCRP = "marketplace_cancellation_and_refund_policy_for_website";
	String MRP = "marketplace_return_policy";
	String MTOS = "marketplace_terms_of_service";
	
	
	@Override
	public String getEditPolicyTemplate(String type, Model model) {
		try {
			Policys policy =null;
			if(type.equals(MPFS) || type.equals(MPFW) || type.equals(MRP) || type.equals(MSDPFW) || type.equals(MCRP) || type.equals(MTOS)) {
				policy = policyRepository.findByPolicyType(type);
				if(policy==null) {
					model.addAttribute("policy", new Policys());
				}else {
					model.addAttribute("policy",policy);
				}
				model.addAttribute("policyType", type);
				model.addAttribute("status", "none");
			}else {
				 throw new Exception("Invalid policy type!!");
			}
			return "UserPages/policyTemplate";
			
		}catch(Exception e) {
			System.out.println("Error (template policy details) : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String savePolicyDetails(Policys policy, Model model) {
		try {
			Policys existPolicy = policyRepository.findByPolicyType(policy.getPolicyType());
			if(existPolicy!=null) {
				existPolicy.setPolicy(policy.getPolicy());
				policyRepository.save(existPolicy);
				model.addAttribute("status", "updated");
			}else {
				policyRepository.save(policy);
				model.addAttribute("status", "add_new");
			}
			model.addAttribute("policy", policy);
			return "UserPages/policyTemplate";
		}catch(Exception e) {
			System.out.println("Error (save policy details) : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}
}
