package com.marketplace.api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marketplace.api.entitys.OrderProducts;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts, String> {

	Page<OrderProducts> findBySellerIdOrStatusOrOrderDateGreaterThanOrOrderDateLessThan(Integer sellerId,
			String status, Date toDate, Date fromDate, Pageable pageable);
	
	@Query("select o from OrderProducts o where o.sellerId = ?1 and o.quantity >= 1 and o.status = ?2 and o.orderDate <= ?3 and o.orderDate >= ?4")
	Page<OrderProducts> getOrderedProductDetails(Integer sellerId, String status, Date toDate, Date fromDate, Pageable pageable);
	
	@Query("select o from OrderProducts o where o.sellerId = ?1 and o.quantity >= 1 and o.orderDate <= ?2 and o.orderDate >= ?3")
	Page<OrderProducts> getOrderedProductDetails(Integer sellerId, Date toDate, Date fromDate, Pageable pageable);
	
	@Query("select o from OrderProducts o where o.sellerId = ?1 and o.quantity >= 1 and o.status = ?2")
	Page<OrderProducts> getOrderedProductDetails(Integer sellerId, String status, Pageable pageable);
	
	@Query("select o from OrderProducts o where o.sellerId = ?1 and o.quantity >= 1")
	Page<OrderProducts> getOrderedProductDetails(Integer sellerId, Pageable pageable);
	
	List<OrderProducts> findBySellerId(Integer sellerId);
	Page<OrderProducts> findBySellerId(Integer sellerId, Pageable pageable);
	
	OrderProducts findByOrderProductId(String orderProductId);

		
}
