package marketplace.admin.api.marketplace_Admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import marketplace.admin.api.marketplace_Admin.enititys.ReferralOrders;

@Repository
public interface ReferralOrderRepository extends JpaRepository<ReferralOrders, Integer> {

	Page<ReferralOrders> findByAffiliateMarketerId(int reportingAccntId, Pageable pageable);

	ReferralOrders findByReferralOrderId(int referralId);

}
