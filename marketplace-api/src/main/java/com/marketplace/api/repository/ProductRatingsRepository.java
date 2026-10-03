package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.ProductRatings;

public interface ProductRatingsRepository extends JpaRepository<ProductRatings, Integer> {
	ProductRatings findByUserIdAndProductId(int userId,String productId);
}
