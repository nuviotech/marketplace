package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.Settlement;

public interface SettlementRepository extends JpaRepository<Settlement, Integer> {

}
