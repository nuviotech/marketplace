package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.entitys.Subscribers;

public interface SubcriberRepository extends JpaRepository<Subscribers, Integer> {
	public Subscribers findByEmail(String email);
}
