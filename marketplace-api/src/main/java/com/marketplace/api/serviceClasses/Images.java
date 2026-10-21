package com.marketplace.api.serviceClasses;
/*
 * This Image class is for create the json file of urls
 * */
public class Images {
	String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "{ \"url\":\"" + url
				+ "\" }";
	}	
}
