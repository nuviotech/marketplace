package com.marketplace.api.serviceClasses;

import java.io.ByteArrayInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.marketplace.api.entitys.Invoice;
import com.marketplace.api.repository.InvoiceRepository;
import jakarta.mail.util.ByteArrayDataSource;


public class EmailProps {
	@Autowired
	PdfService pdfService;
	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Value("${marketplace.api.email.id:}")
	//@Value("${mail.email.id}")
	String username;
	@Value("${marketplace.api.email.password:}")
	//@Value("${mail.email.password}")
	String password;

	public Properties getEmailProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.host", "smtp.gmail.com");
		return props;
	}

	public boolean sendEmail(String toEmail, String subject, String mailBody) {
		try {
			Properties props = getEmailProperties();

			Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("support@nuvio.in"));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress("support@nuvio.in"));
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
	
	public boolean sendInvoiceEmail(String toEmail, String subject, String mailBody,int invoiceId) {
		try {
			Properties props = getEmailProperties();

			Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("support@nuvio.in"));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress("support@nuvio.in"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

			message.setSubject(subject);
			
			// Create the message part
	        BodyPart messageBodyPart = new MimeBodyPart();

	        // Now set the actual message
	        messageBodyPart.setText(mailBody);
	        

	        // Create a multipar message
	        Multipart multipart = new MimeMultipart();

	        // Set text message part
	        multipart.addBodyPart(messageBodyPart);
	       

	        // Part two is attachment
	        messageBodyPart = new MimeBodyPart();
	        //String filename = "/Users/rick/Tmp/test.pdf";
	        Invoice invoice= invoiceRepository.findById(invoiceId).get();
	        ByteArrayInputStream bis= pdfService.createTaxInvoicePdf(invoice);
	        ByteArrayDataSource bds=new ByteArrayDataSource(bis.readAllBytes(),"application/pdf");
	        //DataSource source= (DataSource)pdfService.createTaxInvoicePdf(invoice);
	       // try (InputStream inputStream = new FileInputStream(filename)) {
	       // = new ByteArrayDataSource(inputStream, "application/pdf");
	       // }

	        //printDataSourceByteCount(source);

	        messageBodyPart.setDataHandler(new DataHandler(bds));
	        messageBodyPart.setFileName("invoice.pdf");
	        multipart.addBodyPart(messageBodyPart);

	        // Send the complete message parts
	        message.setContent(multipart);

	        // Send message
	        Transport.send(message);

	        System.out.println("Sent message successfully....");

			
			
			
			//message.setContent(mailBody, "text/html");
			//Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR (mail : " + subject + ") cause : " + e.getMessage());
			return false;
		}
	}
	
}
