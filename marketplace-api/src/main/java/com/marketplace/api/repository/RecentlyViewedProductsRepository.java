package com.marketplace.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.marketplace.api.entitys.RecentlyViewedProducts;
import com.marketplace.api.entitys.User;

@Repository
public interface RecentlyViewedProductsRepository extends JpaRepository<RecentlyViewedProducts, Integer> {
	
	/**
	 * Find all recently viewed products for a specific user, ordered by most recently viewed first
	 * Limit to last 20 products
	 */
	@Query(value = "SELECT * FROM recently_viewed_products WHERE user_id = :userId ORDER BY viewed_date DESC LIMIT 20", nativeQuery = true)
	List<RecentlyViewedProducts> findRecentlyViewedByUser(@Param("userId") int userId);
	
	/**
	 * Delete all recently viewed products older than X days for a user (optional cleanup)
	 */
	@Query(value = "DELETE FROM recently_viewed_products WHERE user_id = :userId AND viewed_date < DATE_SUB(NOW(), INTERVAL :days DAY)", nativeQuery = true)
	void deleteOldRecentlyViewed(@Param("userId") int userId, @Param("days") int days);
	
	/**
	 * Find if a product is already in recently viewed list
	 */
	RecentlyViewedProducts findByUserAndProductId(User user, String productId);
}
