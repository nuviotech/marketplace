package com.marketplace.api.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="product_ratings")
public class ProductRatings {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_ratings_id")
	int productRatingsId;
	
	@Column(name="user_id")
	int userId;
	
	@Column(name="user_name")
	String userName; 
	
	@Column(name="product_id")
	String productId;
	
	@Column(name="rating",columnDefinition = "FLOAT DEFAULT 0.0")
	float rating;
	
	@Column(name="review",length=1000)
	String review;

	public int getProductRatingsId() {
		return productRatingsId;
	}

	public void setProductRatingsId(int productRatingsId) {
		this.productRatingsId = productRatingsId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	
	
	
}
