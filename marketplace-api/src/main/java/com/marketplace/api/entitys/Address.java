package com.marketplace.api.entitys;

public interface Address {
	
	public String getFirstName();

	public void setFirstName(String firstName);

	public String getLastName();

	public void setLastName(String lastName) ;

	public String getCountry();

	public void setCountry(String country);

	public String getStreetAddress();

	public void setStreetAddress(String streetAddress);

	public String getState();

	public void setState(String state);

	public String getPincode();

	public void setPincode(String pincode);

	public String getEmail();

	public void setEmail(String email);

	public String getCity();
	public void setCity(String city);

	public User getUser();
	
	public void setUser(User user);
}
