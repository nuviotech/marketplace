package com.marketplace.api.serviceClasses;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

/*
 * all product details contain this Products class
 * product pojo
 * */
public class Products{
	String id;
	String cud;
	String sku;
	String title;
	int quantity;
	
	double sale_price;
	int categoryId;
	int depot;

	boolean is_active=true;

	String slug;//this is sub category
	Date createdDtTime;
	
	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product2")
	@Transient
	List<Images> images=new ArrayList<>();
	
	//for images of product
	String imgMain;
	String imgUrl1;
	String imgUrl2;
	String imgUrl3;
	String imgUrl4;
	String imgUrl5;
	String imgUrl6;
	String imgUrl7;
	String imgUrl8;
	
	@Column(name="MRP")
	double price;
	
	@Column(name="Material")
	String material;
	
	@Column(name="Brand")
	String brand;
	
	@Column(name="Color")
	String color;
	
	@Column(name="Description")
	String description;
	
	
	
	
	
	
	public String getProductRefId() {
		return id;
	}

	public void setProductRefId(String productRefId) {
		this.id = productRefId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getMrp() {
		return price;
	}

	public void setMrp(double mrp) {
		this.price = mrp;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getSale_price() {
		return sale_price;
	}

	public void setSale_price(double sale_price) {
		this.sale_price = sale_price;
	}

	

	public int getDepot() {
		return depot;
	}

	public void setDepot(int depot) {
		this.depot = depot;
	}

	

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	public String getCud() {
		return cud;
	}

	public void setCud(String cud) {
		this.cud = cud;
	}

	public Date getCreatedDtTime() {
		return createdDtTime;
	}

	public void setCreatedDtTime(Date createdDtTime) {
		this.createdDtTime = createdDtTime;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getImgMain() {
		return imgMain;
	}

	public void setImgMain(String imgMain) {
		this.imgMain = imgMain;
	}

	public String getImgUrl1() {
		return imgUrl1;
	}

	public void setImgUrl1(String imgUrl1) {
		this.imgUrl1 = imgUrl1;
	}

	public String getImgUrl2() {
		return imgUrl2;
	}

	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}

	public String getImgUrl3() {
		return imgUrl3;
	}

	public void setImgUrl3(String imgUrl3) {
		this.imgUrl3 = imgUrl3;
	}

	public String getImgUrl4() {
		return imgUrl4;
	}

	public void setImgUrl4(String imgUrl4) {
		this.imgUrl4 = imgUrl4;
	}

	public String getImgUrl5() {
		return imgUrl5;
	}

	public void setImgUrl5(String imgUrl5) {
		this.imgUrl5 = imgUrl5;
	}

	public String getImgUrl6() {
		return imgUrl6;
	}

	public void setImgUrl6(String imgUrl6) {
		this.imgUrl6 = imgUrl6;
	}

	public String getImgUrl7() {
		return imgUrl7;
	}

	public void setImgUrl7(String imgUrl7) {
		this.imgUrl7 = imgUrl7;
	}

	public String getImgUrl8() {
		return imgUrl8;
	}

	public void setImgUrl8(String imgUrl8) {
		this.imgUrl8 = imgUrl8;
	}

	public List<Images> getImages() {
		return images;
	}

	public void setImages(List<Images> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"sku\":\"" + sku + "\", \"title\":\"" + title + "\", \"quantity\":" + 12 + ", \"is_featured\":\"f\""
				+ ", \"is_hot\":\"hot\""  + ", \"sale_price\":" + sale_price + ", \"vendor\":\""  + "\", \"depot\":" + depot + ", \"is_active\":" + is_active + ", \"slug\":\"" + slug + "\", \"images\":" + images + ", \"user\":[" +"], \"price\":" + price
				+ ", \"material\":\"" + material + "\", \"brand\":\"" + brand + "\", \"color\":\"" + color + "\", \"description\":\"" + description
				+ "\"}";
	} 
	
}
