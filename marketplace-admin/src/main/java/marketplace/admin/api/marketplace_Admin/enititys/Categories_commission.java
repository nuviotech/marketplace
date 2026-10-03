package marketplace.admin.api.marketplace_Admin.enititys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Categories_commission {
	@Id
	String ccid;
	
	String categoriesName;
	int categoriesCommission;
	public String getCcid() {
		return ccid;
	}
	public void setCcid(String ccid) {
		this.ccid = ccid;
	}
	public String getCategoriesName() {
		return categoriesName;
	}
	public void setCategoriesName(String categoriesName) {
		this.categoriesName = categoriesName;
	}
	public int getCategoriesCommission() {
		return categoriesCommission;
	}
	public void setCategoriesCommission(int categoriesCommission) {
		this.categoriesCommission = categoriesCommission;
	}
	
}
