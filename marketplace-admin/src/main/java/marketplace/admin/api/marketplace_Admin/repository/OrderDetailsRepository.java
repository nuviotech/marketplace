package marketplace.admin.api.marketplace_Admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import marketplace.admin.api.marketplace_Admin.enititys.OrderDetails;


public interface OrderDetailsRepository extends JpaRepository<OrderDetails, String>{
	public Page<OrderDetails> findByPaymentStatus(String paymentStatus,Pageable pageable);
}
