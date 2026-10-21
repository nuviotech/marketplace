package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.OrderDetails;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, String>{

	OrderDetails findByOrderId(String orderId);

}
