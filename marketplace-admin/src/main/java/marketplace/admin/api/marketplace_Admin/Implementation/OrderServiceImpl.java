package marketplace.admin.api.marketplace_Admin.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.enititys.OrderProducts;
import marketplace.admin.api.marketplace_Admin.enititys.ReturnDetails;
import marketplace.admin.api.marketplace_Admin.repository.OrderProductRepository;
import marketplace.admin.api.marketplace_Admin.repository.RefundDetailsRepository;
import marketplace.admin.api.marketplace_Admin.service.OrderDetailsService;
import marketplace.admin.api.marketplace_Admin.service.OrderService;

import marketplace.admin.api.marketplace_Admin.service.EmailActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderProductRepository orderProductRepository;
	@Autowired
	RefundDetailsRepository refundDetailsRepository;
	@Autowired
	OrderDetailsService  orderDetailsService;
	@Autowired
	EmailActions emailActions;

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Override
	public String getAllOrderDetails(int page, String action, Model model) {
		try {
		    Pageable pageable=PageRequest.of(page, 20, Sort.by("orderDate").descending());
		    Page<OrderProducts> orders=null;
		    if(action.equalsIgnoreCase("all"))
		    	orders= orderProductRepository.findAll(pageable);
		    else
		        orders= orderProductRepository.findByStatusIgnoreCase(action, pageable);

			model.addAttribute("totalPage", orders.getTotalPages());
			model.addAttribute("currentPage", page);
			model.addAttribute("action", action);
			model.addAttribute("orderProducts", orders);
			return "UserPages/orderTable";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",HttpStatus.INTERNAL_SERVER_ERROR);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String getAllrefundOrderDetails(int page, String action, Model model) {
		try{
			Pageable pageable=PageRequest.of(page,7);
			Page<ReturnDetails> pages=null;
			if(action.equalsIgnoreCase("all")) {
				pages=refundDetailsRepository.findAll(pageable);
			}
			model.addAttribute("refunds", pages);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPage",pages.getTotalPages());
			return "UserPages/returnOrderTable";
		}catch(Exception e) {
			model.addAttribute("errCode",400);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String refundOrder(int id, Model model,MarketplaceAdmin user) {
		try {
			System.out.println("ID -> "+id);
			String result= orderDetailsService.refundThePlacedOrder(id, user);
			if(result.equalsIgnoreCase("0")) {
				return "redirect:/marketplace/returnOrderDetails?page=0&action=all";
			}else {
				model.addAttribute("errCode","ERR");
				model.addAttribute("errMsg",result);
				return "UserPages/error";
			}
			//return result;
		}catch(Exception e) {
			System.out.println("### ERROR happen in refund");
			e.printStackTrace();
			model.addAttribute("errCode",400);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String updateOrderStatus(String orderProductId, String status) {
		try {
			java.util.Optional<OrderProducts> optional = orderProductRepository.findById(orderProductId);
			if (optional.isPresent()) {
				OrderProducts orderProduct = optional.get();
				String oldStatus = orderProduct.getStatus();
				if (oldStatus == null || !oldStatus.equalsIgnoreCase(status)) {
					orderProduct.setStatus(status);
					orderProductRepository.save(orderProduct);

					try {
						emailActions.sendOrderStatusUpdateEmail(orderProduct, oldStatus, status);
					} catch (Exception ex) {
						logger.error("Failed to send order status update email for OrderProduct ID {}: {}", orderProductId, ex.getMessage());
					}
				}
				return "0";
			}
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
	}

	@Override
	public String bulkUpdateOrderStatus(java.util.List<String> orderProductIds, String status) {
		try {
			if (orderProductIds != null && !orderProductIds.isEmpty()) {
				for (String id : orderProductIds) {
					try {
						java.util.Optional<OrderProducts> optional = orderProductRepository.findById(id);
						if (optional.isPresent()) {
							OrderProducts orderProduct = optional.get();
							String oldStatus = orderProduct.getStatus();
							if (oldStatus == null || !oldStatus.equalsIgnoreCase(status)) {
								orderProduct.setStatus(status);
								orderProductRepository.save(orderProduct);

								try {
									emailActions.sendOrderStatusUpdateEmail(orderProduct, oldStatus, status);
								} catch (Exception ex) {
									logger.error("Failed to send order status update email for OrderProduct ID {}: {}", id, ex.getMessage());
								}
							}
						}
					} catch (Exception ex) {
						logger.error("Error updating status for OrderProduct ID {}: {}", id, ex.getMessage());
					}
				}
			}
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
	}

}
