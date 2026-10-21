package marketplace.admin.api.marketplace_Admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import marketplace.admin.api.marketplace_Admin.enititys.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
	public Page<Seller> findByStatus(String status,Pageable pageable); 
	public Seller findByEmailAddr(String email);
	public Seller findByEmailAddrAndStatus(String email,String status);
	public int countByStatus(String status);
	public Seller findByRegisteredSellerId(int registeredSellerId);
}
