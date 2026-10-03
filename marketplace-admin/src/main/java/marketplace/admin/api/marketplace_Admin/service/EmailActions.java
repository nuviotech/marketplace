package marketplace.admin.api.marketplace_Admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import marketplace.admin.api.marketplace_Admin.enititys.Emails;
import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.enititys.User;
import marketplace.admin.api.marketplace_Admin.repository.EmailRepository;
import marketplace.admin.api.marketplace_Admin.repository.UserRepository;

import marketplace.admin.api.marketplace_Admin.enititys.OrderProducts;

public interface EmailActions {
	
	
    public boolean sendResetPasswordEmailMessage(MarketplaceAdmin u, String verificationCode);
    
    public String sendCustomEmailToUsers(String reciverEmails, Emails email );
    
    @Async
    public void sendOrderStatusUpdateEmail(OrderProducts orderProduct, String oldStatus, String newStatus);
}
