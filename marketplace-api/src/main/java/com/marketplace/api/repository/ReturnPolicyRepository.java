package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.ReturnPolicy;

public interface ReturnPolicyRepository extends JpaRepository<ReturnPolicy, Integer> {
	//to find by category id
	public ReturnPolicy findByCategoryId(int catId);
}
