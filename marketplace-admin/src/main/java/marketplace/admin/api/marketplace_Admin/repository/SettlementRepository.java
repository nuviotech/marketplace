package marketplace.admin.api.marketplace_Admin.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import marketplace.admin.api.marketplace_Admin.enititys.Settlement;

public interface SettlementRepository extends JpaRepository<Settlement, Integer> {
	
	Page<Settlement> findByCreatedDtTimeBetween(Date startDate, Date endDate,Pageable pageable);
	List<Settlement> findByCreatedDtTimeBetween(Date startDate, Date endDate);

	
}
