package marketplace.admin.api.marketplace_Admin.service;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;

public interface OrderService {
	public String getAllOrderDetails(int page,String action,Model model);
	public String getAllrefundOrderDetails(int page,String action,Model model);
	public String refundOrder(int id,Model model,MarketplaceAdmin user);
	public String updateOrderStatus(String orderProductId, String status);
	public String bulkUpdateOrderStatus(List<String> orderProductIds, String status);
}
