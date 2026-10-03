package marketplace.admin.api.marketplace_Admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;

public interface MarketplaceAdminRepository extends JpaRepository<MarketplaceAdmin, Integer> {
	public MarketplaceAdmin findByEmail(String email);
	
	public MarketplaceAdmin findByEmailAndResetPasswordCode(String email,String code);
}
