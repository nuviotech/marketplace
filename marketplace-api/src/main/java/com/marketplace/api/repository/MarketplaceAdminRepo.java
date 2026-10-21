package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.MarketplaceAdmin;

public interface MarketplaceAdminRepo extends JpaRepository<MarketplaceAdmin,Integer> {

}
