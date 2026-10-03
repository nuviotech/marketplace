package com.marketplace.api.serviceClasses;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.marketplace.api.entitys.Invoice;
import com.marketplace.api.entitys.OrderDetails;
import com.marketplace.api.entitys.OrderProducts;
import com.marketplace.api.entitys.User;
import com.marketplace.api.repository.InvoiceRepository;

import java.util.List;
import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

@Service
public class EmailActions extends EmailProps {
	@Autowired 
	InvoiceRepository  invoiceRepository;
	Logger logger=LoggerFactory.getLogger(EmailActions.class);
	
    public boolean sendResetPasswordEmailMessage(User u, String verificationCode) {
		boolean flag = false;
		try {
			String subject = "Reset password";
			String sendarName = "Nuvio sellers ";
			StringBuilder builder =new StringBuilder();
			builder.append("<p>Dear " + u.getFirstName() + ", </p>");
			builder.append("<p> please click the link below to reset your account password </p>");
			String verifyUrl = "https://nuvio.in/page/Reset_password?verifivationId=" + verificationCode;

			builder.append("<h3> <a href=\"" + verifyUrl + "\">Reset Password</a></h3>");
			builder.append("<p> Thank you<br>Nuvio sellers</p>");
			String toEmail =u.getEmail();
			String mailBody=builder.toString();
	 
			
			flag = sendEmail(toEmail, subject, mailBody);

		} catch (Exception e) {
			logger.error("(when reset the password) ERROR HAPPEN..................");
			e.printStackTrace();
			// TODO: handle exception
		}
		return flag;
	}
    
    @Async
    public void sendOrderSuccessEmail(String toEmail,OrderDetails order) {
    	try {
    		String subject = "Order Confirmation";
			String sendarName = "Nuvio sellers";
			StringBuilder builder=new StringBuilder();
			builder.append("<html>\r\n"
					+ "<head>\r\n"
					+ "	<style>\r\n"
					+ "		.center{\r\n"
					+ "			text-align: center;\r\n"
					+ "		}\r\n"
					+ "		.end{\r\n"
					+ "			text-align:right;\r\n"
					+ "		}\r\n"
					+ "		.text-danger{\r\n"
					+ "			color:red;\r\n"
					+ "		}\r\n"
					+ "		.my-3{\r\n"
					+ "			margin:3px 0px;\r\n"
					+ "		}\r\n"
					+ "		.mt-5{\r\n"
					+ "			margin-top:50px;\r\n"
					+ "		}\r\n"
					+ "		.container{\r\n"
					+ "			margin:0% 5%;\r\n"
					+ "		}\r\n"
					+ "		#products {\r\n"
					+ "		  font-family: Arial, Helvetica, sans-serif;\r\n"
					+ "		  border-collapse: collapse;\r\n"
					+ "		  width: 100%;\r\n"
					+ "		}\r\n"
					+ "\r\n"
					+ "		#products td, #products th {\r\n"
					+ "		  border: 1px solid #ddd;\r\n"
					+ "		  padding: 8px;\r\n"
					+ "		}\r\n"
					+ "\r\n"
					+ "		#products tr:nth-child(even){background-color: #f2f2f2;}\r\n"
					+ "\r\n"
					+ "		#products tr:hover {background-color: #ddd;}\r\n"
					+ "\r\n"
					+ "		#products th {\r\n"
					+ "		  padding-top: 12px;\r\n"
					+ "		  padding-bottom: 12px;\r\n"
					+ "		  text-align: left;\r\n"
					+ "		  background-color: #04AA6D;\r\n"
					+ "		  color: white;\r\n"
					+ "		}\r\n"
					+ "		@media only screen and (max-width: 600px) {\r\n"
					+ "			.container {\r\n"
					+ "				margin: 0 1px;\r\n"
					+ "			}\r\n"
					+ "		}"
					+ "	</style>\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "	<div class=\"center\" style=\"margin-bottom: 50px;\">\r\n"
					+ "		<img style=\"width:90px; margin-bottom: 20px;\" src=\"https://nuvio.in/static/img/nuvioseller.png\" alt=\"NUVIO\" />\r\n"
					+ "		<h2 class=\"my-3\"><b>Thank you for shopping with us!</b></h2>\r\n"
					+ "	</div>\r\n"
					+ "	<div class=\"container\">\r\n"
					+ "	\r\n"
					+ "		<p>Order ID : <b> "+order.getOrderId()+" </b>"
					+ "     <p>Name : "+order.getFname()+" "+order.getLname()+" </p>"
					+ "		<p>Shipping address : "+order.getShippingAddressPrimary()+"</p>"
					+ "     <p>Payment Mode :"+order.getPaymentType()+"</p>"
					+ "     <p>Payment Status :"+order.getPaymentStatus()+"</p>");
			
				
				List<Invoice> invoiceList = invoiceRepository.findByInvoiceOrderId(order);
				int col=0;
				for(Invoice invoices : invoiceList) {
					builder.append("<p class=\"my-3\" >Tax Type : "+invoices.getTaxType()+"</p><br/>"
					+ "<table id=\"products\">\r\n"
					+ "		  <tr>\r\n"
					+ "			<th>Description</th>\r\n"
					+ "			<th style=\"min-width:30px\">Unit Price</th>\r\n"
					+ "			<th style=\"min-width:20px\">Qty</th>\r\n");
							
						if(invoices.getTaxType().equals("CGST,SGST")) {
								builder.append("<th style=\"min-width:20px\">CGST</th>\r\n"
										+ "<th style=\"min-width:20px\">SGST</th>\r\n");
								col=6;
							}else {
								builder.append("<th style=\"min-width:20px\">IGST</th>\r\n");
								col=5;
							}
					
	
					builder.append("<th style=\"min-width:30px\">Tax Amount</th>\r\n"
					+ "			<th style=\"min-width:30px\">Total Amount</th>"
					+ "		  </tr>\r\n");

					for(OrderProducts product : invoices.getItems() ) {
						builder.append( "<tr>\r\n"
								+ "	<td>"+product.getProduct_name()+" </td>\r\n"
								+ "	<td class=\"center\">"+(product.getSole_price()-product.getTaxAmt())+"</td>\r\n"
								+ "	<td class=\"center\">"+product.getQuantity()+"</td>\r\n");
								
								if(invoices.getTaxType().equals("CGST,SGST")) {
									builder.append("<td style=\"min-width:20px\">"+(Integer.parseInt(product.getTaxRate())/2)+"%</td>\r\n"
											+ "<td style=\"min-width:20px\">"+(Integer.parseInt(product.getTaxRate())/2)+"%</td>\r\n");
								}else {
									builder.append("<td style=\"min-width:20px\">"+product.getTaxRate() +"%</td>\r\n");
								}
								
								builder.append(" <td class=\"center\">"+product.getTaxAmt()*product.getQuantity()+"</td>\r\n"
								+ " <td class=\"center\">"+product.getSole_price()*product.getQuantity()+"</td>\r\n"
								+ "</tr>\r\n");
					}
					
					builder.append("<tr>\r\n"
							+ "	<td>Shipping Charge </td>\r\n"
							+ "	<td class=\"center\">40.18</td>\r\n"
							+ "	<td class=\"center\"></td>\r\n");
							
							if(invoices.getTaxType().equals("CGST,SGST")) {
								builder.append("<td style=\"min-width:20px\">9%</td>\r\n"
										+ "<td style=\"min-width:20px\">9%</td>\r\n");
							}else {
								builder.append("<td style=\"min-width:20px\">18%</td>\r\n");
							}
							
							builder.append(" <td class=\"center\">8.82</td>\r\n"
							+ " <td class=\"center\">49.00</td>\r\n"
							+ "</tr>\r\n");
					
						
					builder.append("<tr>\r\n"
						+ "			<td colspan=\""+col+"\" class=\"text-danger end\"><b>Total invoice price : </b></td>\r\n"
						+ "			<td class=\"center\">"+order.getTotalBill()+"</td>\r\n"
						+ "		  </tr>\r\n"
						+ "		  \r\n"
						+ "		</table>\r\n");
				}
					builder.append("\r\n"
					+ "		<div class=\"mt-5\">\r\n"
					+ "			<p>Thank You!</p>\r\n"
					+ "		</div>\r\n"
					+ "	</div>\r\n"
					+ "	\r\n"
					+ "</body>\r\n"
					+ "</html>");
			String mailBody = builder.toString(); 
			sendEmail(toEmail, subject, mailBody);
    	}catch(Exception e) {
    		logger.error("Error occur when sending email(order) : "+e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    public void SendMailAtachment(String email,String sub,String mailBody,int invoiceId) {
		sendInvoiceEmail(email, sub, mailBody, invoiceId);
	}
    
    @Async
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
    		sendEmail(toEmail, subject, mailBody);
    		logger.info("Order status update email sent to {} for OrderProduct ID {}", toEmail, orderProduct.getOrderProductId());
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
