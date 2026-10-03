package marketplace.admin.api.marketplace_Admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import marketplace.admin.api.marketplace_Admin.enititys.OrderProducts;


public interface OrderProductRepository extends JpaRepository<OrderProducts, String> {
	public Page<OrderProducts> findByStatus(String status,Pageable pageable);
	public Page<OrderProducts> findByStatusIgnoreCase(String status,Pageable pageable);
}
