package marketplace.admin.api.marketplace_Admin.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.Categories_commission;
import marketplace.admin.api.marketplace_Admin.repository.CategoriesCommissionRepository;
import marketplace.admin.api.marketplace_Admin.service.CategoriesCommissionService;

@Service
public class CategoriesCommissionServiceImpl implements CategoriesCommissionService {
	@Autowired
	CategoriesCommissionRepository categoriesCommissionRepository;
	
	@Override
	public String getCategoriesCommission(Model model) {
		try {
			List<Categories_commission> CC = categoriesCommissionRepository.findAll(); 
			model.addAttribute("categories_commission", CC);
			return "UserPages/categories_commission";
		}catch(Exception e) {
			System.out.println("Error (categories commission ) : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",400);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String editcategoriesCommission(int commission, String catId) {
		try {
			Categories_commission cc = categoriesCommissionRepository.findById(catId).get();
			if(commission<=25) {
				cc.setCategoriesCommission(commission);
				categoriesCommissionRepository.save(cc);
			}else {
				return "1";
			}
			return "0";
		}catch(Exception e) {
			e.printStackTrace();
			return "-1";
		}
	}

}
