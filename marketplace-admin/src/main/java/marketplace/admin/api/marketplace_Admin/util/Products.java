package marketplace.admin.api.marketplace_Admin.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Products {
	
	String productRefId;
	
	String brand;
	
	String color;
	
	Date createdDtTime;
	
	Date updatedDtTime;
	
	String cud;
	
	String depot;
	
	String sku;
	
	String description;
	
	String material;
	
	float price;
	
	float sale_price;
	
	String title;
	
	String vendor;
	
	String storeId;
	
	String mainImage;
	
	String  imageUrl1;
	
	String imageUrl2;
	
	String imageUrl3;
	
	String imageUrl4;
	
	String imageUrl5;
	
	String imageUrl6;
	
	String imageUrl7;
	
	String imageUrl8;
	
	int stockId;
	
	//in product table case user id is email not number id
	String userId;
	
	String hsnCode;
	
	int categoryId;
	
	String bulletpoints;
	
	String keywords;
	
	int warehouseId;
	
	String slug;// this is sub category
	
	String totalRatingRation;
	
	String specifications;
	
	//transient 
	@Transient
	List<ProductImages> images = new ArrayList<>();
	@Transient
	String stockDetails;
	@Transient
	int quantity;
	@Transient
	String companyName;
	@Transient
	String hsnRate;
	@Transient
	String id;
	@Transient
	String operation;//this use for excel file operation add update delete
	public String getProductRefId() {
		return productRefId;
	}
	public void setProductRefId(String productRefId) {
		this.productRefId = productRefId;
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
	public Date getCreatedDtTime() {
		return createdDtTime;
	}
	public void setCreatedDtTime(Date createdDtTime) {
		this.createdDtTime = createdDtTime;
	}
	public Date getUpdatedDtTime() {
		return updatedDtTime;
	}
	public void setUpdatedDtTime(Date updatedDtTime) {
		this.updatedDtTime = updatedDtTime;
	}
	public String getCud() {
		return cud;
	}
	public void setCud(String cud) {
		this.cud = cud;
	}
	public String getDepot() {
		return depot;
	}
	public void setDepot(String depot) {
		this.depot = depot;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getSale_price() {
		return sale_price;
	}
	public void setSale_price(float sale_price) {
		this.sale_price = sale_price;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getMainImage() {
		return mainImage;
	}
	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	public String getImageUrl1() {
		return imageUrl1;
	}
	public void setImageUrl1(String imageUrl1) {
		this.imageUrl1 = imageUrl1;
	}
	public String getImageUrl2() {
		return imageUrl2;
	}
	public void setImageUrl2(String imageUrl2) {
		this.imageUrl2 = imageUrl2;
	}
	public String getImageUrl3() {
		return imageUrl3;
	}
	public void setImageUrl3(String imageUrl3) {
		this.imageUrl3 = imageUrl3;
	}
	public String getImageUrl4() {
		return imageUrl4;
	}
	public void setImageUrl4(String imageUrl4) {
		this.imageUrl4 = imageUrl4;
	}
	public String getImageUrl5() {
		return imageUrl5;
	}
	public void setImageUrl5(String imageUrl5) {
		this.imageUrl5 = imageUrl5;
	}
	public String getImageUrl6() {
		return imageUrl6;
	}
	public void setImageUrl6(String imageUrl6) {
		this.imageUrl6 = imageUrl6;
	}
	public String getImageUrl7() {
		return imageUrl7;
	}
	public void setImageUrl7(String imageUrl7) {
		this.imageUrl7 = imageUrl7;
	}
	public String getImageUrl8() {
		return imageUrl8;
	}
	public void setImageUrl8(String imageUrl8) {
		this.imageUrl8 = imageUrl8;
	}
	public int getStockId() {
		return stockId;
	}
	public void setStockId(int stockId) {
		this.stockId = stockId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getBulletpoints() {
		return bulletpoints;
	}
	public void setBulletpoints(String bulletpoints) {
		this.bulletpoints = bulletpoints;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public int getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getTotalRatingRation() {
		return totalRatingRation;
	}
	public void setTotalRatingRation(String totalRatingRation) {
		this.totalRatingRation = totalRatingRation;
	}
	public String getSpecifications() {
		return specifications;
	}
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	public List<ProductImages> getImages() {
		return images;
	}
	public void setImages(List<ProductImages> images) {
		this.images = images;
	}
	public String getStockDetails() {
		return stockDetails;
	}
	public void setStockDetails(String stockDetails) {
		this.stockDetails = stockDetails;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getHsnRate() {
		return hsnRate;
	}
	public void setHsnRate(String hsnRate) {
		this.hsnRate = hsnRate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
}
