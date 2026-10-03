package marketplace.admin.api.marketplace_Admin.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import marketplace.admin.api.marketplace_Admin.enititys.Seller;
import marketplace.admin.api.marketplace_Admin.enititys.User;
import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.repository.MarketplaceAdminRepository;
import marketplace.admin.api.marketplace_Admin.repository.UserRepository;
import marketplace.admin.api.marketplace_Admin.service.EmailActions;

@Controller
public class MainController {
	@Autowired
	MarketplaceAdminRepository marketplaceAdminRepository;
	@Autowired
	EmailActions emailActions;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Value("${g.verification.captcha}")
	String verificationCaptchaSiteKey;
	@Value("${marketplace.name}")
	String MARKETPLACE_NAME;
	
	@GetMapping("/*")
	public String pageNotFound(Model model) {
		return "Error_404";
	}
	@GetMapping("/marketplace/*/*")
	public String pageNotFound2() {
		return "Error_404";
	}
	
	@GetMapping("/")
	public String loginPage(Model model) {
		model.addAttribute("title",MARKETPLACE_NAME+" Marketplace Admin | login");
		model.addAttribute("vCaptchaSiteKey", verificationCaptchaSiteKey);
		return "login";
	}
	
	@GetMapping("/forgotePassword")
	public String forgotePasswordPage(Model model) {
		model.addAttribute("title",MARKETPLACE_NAME+" Marketplace Admin | forgote password");
		return "forgotePassword";
	}
	
	@PostMapping("/do_forgotePassword_1")
	public String forgotePasswordRequestFromUser(@RequestParam("email") String email,Model model) {
		try {
			MarketplaceAdmin user = marketplaceAdminRepository.findByEmail(email);
			if(user!=null){
				String code = UUID.randomUUID().toString();
				user.setResetPasswordCode(code);
				emailActions.sendResetPasswordEmailMessage(user, code);
				marketplaceAdminRepository.save(user);
				model.addAttribute("title",MARKETPLACE_NAME+" Marketplace Admin | forgote password");
				model.addAttribute("success","mail send successfully.");

				return "forgotePassword";
			}else {
				model.addAttribute("title",MARKETPLACE_NAME+" Marketplace Admin | forgote password");
				model.addAttribute("error","invalid email address !!");
				return "forgotePassword";
			}
		}catch(Exception e){
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "Error_404";
		}
	}
	
	@PostMapping("/do_resetPassword_1")
	public String resetPasswordRequestFromUser(@RequestParam("token_email") String email,@RequestParam("newPassword") String password,@RequestParam("code") String code,Model model) {
		try {
			MarketplaceAdmin user = marketplaceAdminRepository.findByEmailAndResetPasswordCode(email, code);
			if(user!=null){
				user.setPassword(bCryptPasswordEncoder.encode(password));
				user.setResetPasswordCode(null);
				marketplaceAdminRepository.save(user);
				model.addAttribute("title",MARKETPLACE_NAME+" Marketplace Admin | Reset password");
				model.addAttribute("success","Password change successfully, please login with new credentials.");
				return "forgotePassword";
			}else {
				model.addAttribute("title",MARKETPLACE_NAME+" Marketplace Admin | Reset password");
				model.addAttribute("error","invalid request!!");
				return "forgotePassword";
			}
		}catch(Exception e){
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "Error_404";
		}
	}
}
