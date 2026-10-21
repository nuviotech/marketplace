package com.marketplace.api.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.marketplace.api.entitys.User;

public interface UserRepository extends JpaRepository<User,Integer> {

	public User findByEmail(String userEmail);
	public User findByResetPasswordVerificationId(String verificationId);
	public User findByGoogleId(String googleId);
	
	/**
	 * Fetch user with complete order hierarchy loaded
	 * Uses JOIN FETCH to avoid lazy loading issues during JSON serialization
	 * Only used when complete order data is needed (e.g., getUserDetails endpoint)
	 */
	//@Query("SELECT DISTINCT u FROM User u " +
	  //     "LEFT JOIN FETCH u.orders o " +
	   //    "LEFT JOIN FETCH o.invoices i " +
	    //   "WHERE u.email = :email")
	//public User findByEmailWithOrders(@Param("email") String email);

@Query("SELECT u FROM User u WHERE u.email = :email")
User findByEmailWithOrders(@Param("email") String email);

}
