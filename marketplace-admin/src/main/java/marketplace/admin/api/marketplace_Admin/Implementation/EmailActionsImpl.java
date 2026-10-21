package marketplace.admin.api.marketplace_Admin.Implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import marketplace.admin.api.marketplace_Admin.enititys.Emails;
import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.enititys.User;
import marketplace.admin.api.marketplace_Admin.repository.EmailRepository;
import marketplace.admin.api.marketplace_Admin.repository.UserRepository;
import marketplace.admin.api.marketplace_Admin.service.EmailActions;

import marketplace.admin.api.marketplace_Admin.enititys.Invoice;
import marketplace.admin.api.marketplace_Admin.enititys.OrderDetails;
import marketplace.admin.api.marketplace_Admin.enititys.OrderProducts;
import marketplace.admin.api.marketplace_Admin.repository.MarketplaceAdminRepository;
import org.springframework.scheduling.annotation.Async;

@Service
public class EmailActionsImpl extends EmailPropsImpl implements EmailActions {
	@Autowired
	EmailRepository emailRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MarketplaceAdminRepository marketplaceAdminRepository;
	
	@Value("${website.url}")
	String URL;
	
	Logger logger =LoggerFactory.getLogger(EmailActionsImpl.class);
	
    public boolean sendResetPasswordEmailMessage(MarketplaceAdmin u, String verificationCode) {
		boolean flag = false;
		
		try {
			if(URL.length()==0) {
				throw new Exception("website url is null !!");
			}
			
			String subject = "Reset password";
			String sendarName = "Nuvio sellers ";
			StringBuilder builder =new StringBuilder();
			builder.append("<p>Dear " + u.getName() + ", </p>");
			builder.append("<p> please click the link below to reset your account password </p>");
			String verifyUrl = URL+"/forgotePassword?verifivationId=" + verificationCode+"&email="+u.getEmail();
			builder.append("<h3> <a href=\"" + verifyUrl + "\">Reset Password</a></h3>");
			builder.append("<p> Thank you<br>Nuvio sellers</p>");
			String toEmail =u.getEmail();
			String mailBody=builder.toString();
	 
			
			flag = sendEmail(toEmail, subject, mailBody,u);

		} catch (Exception e) {
			System.out.println("(when reset the password) ERROR HAPPEN..................");
			e.printStackTrace();
			// TODO: handle exception
		}
		return flag;
	}
    
    public String sendCustomEmailToUsers(String emailRecivers,Emails email) {
    	try {
    		if(email!=null) {    			
    			String str=sendCustomEmail(emailRecivers, email.getSubject(),email.getEmailMsg(),email.getSendeEmailType());
    			if(str==null) {
					logger.info("email send to : "+emailRecivers);
				}else {
					logger.warn("email not send to : "+emailRecivers);
				}
    		}
    		return null;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return e.getMessage();
    	}
    }
    
    @Async
    @Override
    public void sendOrderStatusUpdateEmail(OrderProducts orderProduct, String oldStatus, String newStatus) {
    	try {
    		if (orderProduct == null || newStatus == null) {
    			return;
    		}
    		if (oldStatus != null && oldStatus.equalsIgnoreCase(newStatus)) {
    			logger.info("Order status has not changed ({}), skipping email notification.", newStatus);
    			return;
    		}

    		Invoice invoice = orderProduct.getInvoiceDetails();
    		OrderDetails orderDetails = (invoice != null) ? invoice.getInvoiceOrderId() : null;
    		User user = (orderDetails != null) ? orderDetails.getUser() : null;

    		String toEmail = null;
    		if (user != null && user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
    			toEmail = user.getEmail().trim();
    		} else if (orderDetails != null && orderDetails.getUser() != null && orderDetails.getUser().getEmail() != null) {
    			toEmail = orderDetails.getUser().getEmail().trim();
    		}

    		if (toEmail == null || toEmail.isEmpty()) {
    			logger.warn("Unable to find customer email for OrderProduct ID: {}", orderProduct.getOrderProductId());
    			return;
    		}

    		String customerName = "Customer";
    		if (user != null && user.getFirstName() != null && !user.getFirstName().trim().isEmpty()) {
    			customerName = user.getFirstName().trim() + (user.getLastName() != null && !user.getLastName().trim().isEmpty() ? " " + user.getLastName().trim() : "");
    		} else if (orderDetails != null && orderDetails.getFname() != null && !orderDetails.getFname().trim().isEmpty()) {
    			customerName = orderDetails.getFname().trim() + (orderDetails.getLname() != null && !orderDetails.getLname().trim().isEmpty() ? " " + orderDetails.getLname().trim() : "");
    		}

    		String orderId = (orderDetails != null && orderDetails.getOrderId() != null) ? orderDetails.getOrderId() : (invoice != null && invoice.getOrderId() != null ? invoice.getOrderId() : "N/A");
    		String productName = (orderProduct.getProduct_name() != null && !orderProduct.getProduct_name().trim().isEmpty()) ? orderProduct.getProduct_name() : "N/A";
    		int quantity = orderProduct.getQuantity();

    		String subject = getSubjectForStatus(newStatus);
    		String friendlyMessage = getMessageForStatus(newStatus);
    		String displayOldStatus = (oldStatus != null && !oldStatus.trim().isEmpty()) ? oldStatus : "N/A";

    		StringBuilder builder = new StringBuilder();
    		builder.append("<html><head><style>")
    		       .append(".center { text-align: center; }")
    		       .append(".container { margin: 0% 5%; font-family: Arial, sans-serif; color: #333; }")
    		       .append(".card { border: 1px solid #ddd; border-radius: 8px; padding: 20px; margin-top: 20px; background-color: #f9f9f9; }")
    		       .append(".status-badge { display: inline-block; padding: 6px 14px; background-color: #04AA6D; color: white; border-radius: 4px; font-weight: bold; }")
    		       .append(".btn { display: inline-block; padding: 12px 24px; background-color: #f6ab00; color: #000; text-decoration: none; border-radius: 4px; font-weight: bold; margin-top: 15px; }")
    		       .append("#details-table { width: 100%; border-collapse: collapse; margin-top: 15px; }")
    		       .append("#details-table td, #details-table th { border: 1px solid #ddd; padding: 10px; }")
    		       .append("#details-table th { background-color: #f2f2f2; text-align: left; width: 35%; }")
    		       .append("</style></head><body>")
    		       .append("<div class=\"center\" style=\"margin-bottom: 30px;\">")
    		       .append("<img style=\"width:90px; margin-bottom: 15px;\" src=\"https://nuvio.in/static/img/nuvioseller.png\" alt=\"NUVIO\" />")
    		       .append("<h2>Order Status Update</h2></div>")
    		       .append("<div class=\"container\">")
    		       .append("<p>Dear <b>").append(customerName).append("</b>,</p>")
    		       .append("<p>").append(friendlyMessage).append("</p>")
    		       .append("<div class=\"card\">")
    		       .append("<table id=\"details-table\">")
    		       .append("<tr><th>Order ID</th><td><b>").append(orderId).append("</b></td></tr>")
    		       .append("<tr><th>Product</th><td>").append(productName).append("</td></tr>")
    		       .append("<tr><th>Quantity</th><td>").append(quantity).append("</td></tr>")
    		       .append("<tr><th>Previous Status</th><td>").append(displayOldStatus).append("</td></tr>")
    		       .append("<tr><th>Current Status</th><td><span class=\"status-badge\">").append(newStatus.toUpperCase()).append("</span></td></tr>")
    		       .append("</table></div>")
    		       .append("<div class=\"center\" style=\"margin-top: 25px;\">")
    		       .append("<a href=\"https://nuvio.in/account/orders\" class=\"btn\">View Order Details</a></div>")
    		       .append("<div style=\"margin-top: 40px;\"><p>Thank you for shopping with Nuvio!<br>Nuvio Support Team</p></div>")
    		       .append("</div></body></html>");

    		String mailBody = builder.toString();
    		MarketplaceAdmin admin = (marketplaceAdminRepository != null) ? marketplaceAdminRepository.findAll().stream().findFirst().orElse(null) : null;
    		boolean isSent = sendEmail(toEmail, subject, mailBody, admin);
    		if (isSent) {
    			logger.info("Order status update email sent to {} for OrderProduct ID {}", toEmail, orderProduct.getOrderProductId());
    		} else {
    			logger.error("Order status update email failed to send to {} for OrderProduct ID {}", toEmail, orderProduct.getOrderProductId());
    		}
    	} catch (Exception e) {
    		logger.error("Error occurred while sending order status update email: {}", e.getMessage(), e);
    	}
    }

    private String getSubjectForStatus(String status) {
    	if (status == null) return "Update on your Nuvio order";
    	switch (status.trim().toUpperCase()) {
    		case "PENDING":
    			return "Your Nuvio order is pending";
    		case "PROCESSING":
    			return "Your Nuvio order is being processed";
    		case "PACKED":
    			return "Your Nuvio order has been packed";
    		case "SHIPPED":
    			return "Your Nuvio order has been shipped";
    		case "DELIVERED":
    			return "Your Nuvio order has been delivered";
    		case "CANCELLED":
    			return "Your Nuvio order has been cancelled";
    		case "RETURNED":
    			return "Your Nuvio order has been returned";
    		default:
    			return "Your Nuvio order status has been updated to " + status;
    	}
    }

    private String getMessageForStatus(String status) {
    	if (status == null) return "The status of your order item has been updated.";
    	switch (status.trim().toUpperCase()) {
    		case "PENDING":
    			return "Your order has been received and is currently pending confirmation.";
    		case "PROCESSING":
    			return "Great news! Your order is currently being processed by our team.";
    		case "PACKED":
    			return "Your order has been safely packed and is ready for dispatch.";
    		case "SHIPPED":
    			return "Exciting news! Your order has been shipped and is on its way to you.";
    		case "DELIVERED":
    			return "Your order has been successfully delivered. We hope you enjoy your purchase!";
    		case "CANCELLED":
    			return "Your order status has been updated to cancelled. If you have any questions, please contact support.";
    		case "RETURNED":
    			return "We have processed the return for your order item.";
    		default:
    			return "The status of your order item has been updated to " + status + ".";
    	}
    }
}
