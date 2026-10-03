package marketplace.admin.api.marketplace_Admin.service;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;

public interface OrderDetailsService {
	
	
	/**
	 * Refund the order by marketplace admin or seller
	 * refund payment by razorpay
	 * 
	 * */
	public String refundThePlacedOrder(int returnDetailsId, MarketplaceAdmin admin);
	
}
