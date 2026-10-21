package com.marketplace.api.serviceClasses;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketplace.api.entitys.MarketplaceAdmin;
import com.marketplace.api.repository.MarketplaceAdminRepo;

@Service
public class MarketplaceAdminSetting {
	@Autowired
	MarketplaceAdminRepo marketplaceAdminRepo;
	static MarketplaceAdmin marketplaceAdmin;
	Logger logger=LoggerFactory.getLogger(MarketplaceAdminSetting.class);
	
	public boolean setSettings(int marketplaceAdminId) {
		marketplaceAdmin= marketplaceAdminRepo.findById(marketplaceAdminId).get();
		if(marketplaceAdmin==null)
			return false;
		else 
			return true;
	}
	
	public MarketplaceAdmin getSettings() {
		//return marketplaceAdminRepo.findById(marketplaceAdminId).get();
		//System.out.println(marketplaceAdmin.getEmail());
		return marketplaceAdmin;
	}
	
	public String getCodStatus() {
		MarketplaceAdmin md=marketplaceAdminRepo.findById(marketplaceAdmin.getId()).get();
		if(md==null) {
			return "disabled";
		}else {
			return md.getCodStatus();
		}
	}
	
	public Map<String,Object> getMarketplaceSetupInfo() {
		MarketplaceAdmin md=marketplaceAdminRepo.findById(marketplaceAdmin.getId()).get();
		Map<String,Object> map = new HashMap<>();
		if(md==null) {
			logger.warn("Marketplace admin data is NULL");
			map.put("cod","disabled");
			map.put("businessType","both");
			map.put("minimumPurchaseAmt",10);
			map.put("affiliateCommissionRate", 0);
			map.put("codPrepaidAmountPercentage", 0);
		}else {
			map.put("cod",md.getCodStatus());
			map.put("businessType",md.getBusinessType());
			map.put("minimumPurchaseAmt",md.getMinimumPurchaseAmt());
			map.put("affiliateCommissionRate", md.getAffiliateCommissionRate());
			map.put("codPrepaidAmountPercentage", md.getCodPrepaidPercentage());
		}
		return map;
	}
	
}
