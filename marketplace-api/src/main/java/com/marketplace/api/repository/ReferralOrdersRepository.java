package com.marketplace.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marketplace.api.entitys.ReferralOrders;

@Repository
public interface ReferralOrdersRepository extends JpaRepository<ReferralOrders, Integer> {

	Page<ReferralOrders> findByAffiliateMarketerId(int reportingAccountId, Pageable pageable);

}
