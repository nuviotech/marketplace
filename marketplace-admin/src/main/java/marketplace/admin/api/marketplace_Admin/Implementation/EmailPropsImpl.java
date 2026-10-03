package marketplace.admin.api.marketplace_Admin.Implementation;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import marketplace.admin.api.marketplace_Admin.enititys.EmailConfig;
import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.repository.EmailConfigRepository;

public class EmailPropsImpl  {
	@Autowired
	EmailConfigRepository emailConfigRepository;
	
	@Value("${marketplace.api.email.id:support@nuvio.in}")
	private String username;
	
	@Value("${mail.email.password:}")
	private String password;
	
	Logger logger=LoggerFactory.getLogger(EmailPropsImpl.class);
	
	public Properties getEmailProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.host", "smtp.gmail.com");
		return props;
	}

	public boolean sendEmail(String toEmail, String subject, String mailBody,MarketplaceAdmin admin) {
		try {
			Properties props = getEmailProperties();
			String senderEmail = (username != null && !username.trim().isEmpty()) ? username.trim() : "support@nuvio.in";
			String senderPassword = (password != null && !password.trim().isEmpty()) ? password : (admin != null ? admin.getSupportEmailPassword() : "");
			
			Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(senderEmail, senderPassword);
				}
			});
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress(senderEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

			message.setSubject(subject);
			message.setContent(mailBody, "text/html");
			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR (mail : " + subject + ") cause : " + e.getMessage());
			return false;
		}
	}
	
	
	public String sendCustomEmail(String toEmail, String subject, String mailBody,String emailSenderType) {
		try {
			EmailConfig config = emailConfigRepository.findByType(emailSenderType);
			if(config!=null){
				Properties props = getEmailProperties();
	
				//step 1
				Session session=Session.getInstance(props,new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						// TODO Auto-generated method stub
						return new PasswordAuthentication(config.getSenderEmail(),config.getPassword());
					}
				});
				
				session.setDebug(true);
				
				//step 2
				MimeMessage mm = new MimeMessage(session);
				mm.setFrom(new InternetAddress(config.getSenderEmail()));
				mm.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(toEmail));
				mm.setSubject(subject);
				mm.setContent(mailBody, "text/html");
					
				//step 3
				Transport.send(mm);
				return null;
			}else {
				return emailSenderType+" email configuration is null !!";
			}
		} catch (Exception e) {
			System.err.println("ERROR (mail : " + subject + ") cause : " + e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
}
