package com.marketplace.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer>{
	List<Seller> findByStatus(String status);
	public Seller findByEmailAddr(String email);
	public Seller findByRegisteredSellerId(int sellerId);
}
