package com.marketplace.api.entitys;

public interface LoginUser {
	/***
	 * This interface helps to switch normal_user account or affiliate_user account
	 * if user login as affiliate user then get email password authentication from reporting account table
	 * and if login as normal user then get those data from user table
	 * */
	
	public String getEmail();
	public String getPassword();
	public String getRole();
	
	public void setEmail(String email);
	public void setPassword(String password);
	
	
}
