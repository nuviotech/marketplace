package com.marketplace.api.controllers;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.api.entitys.Invoice;
import com.marketplace.api.entitys.Policys;
import com.marketplace.api.entitys.Subscribers;
import com.marketplace.api.entitys.User;
import com.marketplace.api.jwtAuthentication.UserCustomConfigService;
import com.marketplace.api.jwtHelper.JwtUtil;
import com.marketplace.api.repository.InvoiceRepository;
import com.marketplace.api.repository.PolicyRepository;
import com.marketplace.api.repository.SubcriberRepository;
import com.marketplace.api.repository.UserRepository;
import com.marketplace.api.serviceClasses.EmailActions;
import com.marketplace.api.serviceClasses.PdfService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
public class MainController {
	@Autowired
	private UserCustomConfigService userCustomConfigService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	SubcriberRepository subcriberRepository;
	@Autowired
	PdfService pdfService;
	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
	EmailActions emailActions;
	@Autowired
	PolicyRepository policyRepository;
	    
	@PostMapping("/saveSubscriberEmail")
	public String saveSubscriberEmail(@RequestParam("subscriberEmail") String email){
		try {
			Subscribers sub = subcriberRepository.findByEmail(email);
			if(sub==null) {
				Subscribers newSubscriber = new Subscribers();
				newSubscriber.setEmail(email);
				subcriberRepository.save(newSubscriber);
				return "0";
			}else {
				return "1";
			}
			
		}catch(Exception e) {
			System.out.println("subcriber Email (error) : "+e.getMessage());
			e.printStackTrace();
			return "-1";
		}
	}
	
	@GetMapping("/getPolicy/{type}")
	public String getPolicyDetails(@PathVariable("type") String type) {
		try {
			System.out.println("getting policy details");
			Policys policy=policyRepository.findByPolicyType(type);
			if(policy==null) {
				return "data not present!!";
			}else {
				return policy.getPolicy();
			}
		}catch(Exception e) {
			e.printStackTrace();
			return "data not present!!";
		}
	}
	
	
//	static String hashWith256(String textToHash) {
//		try {
//		    MessageDigest digest = MessageDigest.getInstance("SHA-256");
//		    byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
//		    byte[] hashedByetArray = digest.digest(byteOfTextToHash);
//		    String encoded = Base64.getEncoder().encodeToString(hashedByetArray);
//		    return encoded;
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return e.getLocalizedMessage();
//		}
//	}
//	
//	//________________________________________
//	//this is only for testing purpose
//	
//	
//	@GetMapping("/test2")
//	public ResponseEntity<Object> test2() {
//		try {
//			System.out.println("Hello");
//			Invoice invoice=invoiceRepository.findById(22).get();
//			ByteArrayInputStream pdf = pdfService.createTaxInvoicePdf(invoice);
//		
//			HttpHeaders httpHeaders=new HttpHeaders();
//			httpHeaders.add("Content-Disposition", "inline;file=lwd.pdf");
//		//	emailActions.SendMailAtachment("Rnk0775@gmail.com","test","<h1>Hello</h1>", 22);
//		//	return null;
//			return ResponseEntity.ok().headers(httpHeaders).contentType(org.springframework.http.MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			
//			return null;
//		}
//	}
}
