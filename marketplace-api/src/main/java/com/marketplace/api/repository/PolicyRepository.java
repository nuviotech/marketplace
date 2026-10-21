package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.Policys;

public interface PolicyRepository extends JpaRepository<Policys, Integer>  {
	Policys findByPolicyType(String type);
}
