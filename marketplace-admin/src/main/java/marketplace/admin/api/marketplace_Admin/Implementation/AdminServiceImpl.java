package marketplace.admin.api.marketplace_Admin.Implementation;

import java.io.File;
import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.repository.MarketplaceAdminRepository;
import marketplace.admin.api.marketplace_Admin.service.AdminService;
import marketplace.admin.api.marketplace_Admin.service.ApiService;
import marketplace.admin.api.marketplace_Admin.service.ImageSaver;

@Service
public class AdminServiceImpl implements AdminService {
	@Value("${marketplace.api.url}")
	String marketplaceApiUrl;
	@Autowired
	MarketplaceAdminRepository marketplaceAdminRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	ImageSaver imageSaver;
	@Autowired
	ApiService apiService;
	
	@Value("${media.image-folder-path}")
	String MEDIA_IMAGE_FOLDER_PATH;
	
	Logger logger= LoggerFactory.getLogger(AdminServiceImpl.class);
	
	@Override
	public String updateTheUserDetails(MarketplaceAdmin admin, Model model, Principal p) {
		try {
			if(admin.getName().length()>2) {
				
				MarketplaceAdmin user=marketplaceAdminRepository.findByEmail(p.getName());
				user.setName(admin.getName());
				String password=bCryptPasswordEncoder.encode(admin.getPassword());
				System.out.println(password);
				user.setPassword(password);
				
				if(admin.getSupportEmailId().length()>1 && admin.getSupportEmailPassword().length()>0) {
					user.setSupportEmailId(admin.getSupportEmailId());
					user.setSupportEmailPassword(admin.getSupportEmailPassword());
				}
				marketplaceAdminRepository.save(user);
				model.addAttribute("updateUserPData",true);
				return "UserPages/profile";
			}else {
				model.addAttribute("errCode","400");
				model.addAttribute("errMsg","Incorrect form fill, please fill the valid and correct data.");
				return "UserPages/error";
			}
		}catch(Exception e) {
			System.out.println("Error (update user details) : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String saveAdminSetting(String action, MarketplaceAdmin admin, Model model, MultipartFile logo,MultipartFile favicon,Principal p) {
		try {
			
			MarketplaceAdmin user=marketplaceAdminRepository.findByEmail(p.getName());
			if(action.equals("phonePe")) {
				user.setPhonpe_merchantId(admin.getPhonpe_merchantId());
				user.setPhonpe_saltKey(admin.getPhonpe_saltKey());
				user.setPhonpe_index(admin.getPhonpe_index());
				user.setPhonpe_url(admin.getPhonpe_url());
			}else if(action.equals("razorPay")) {
				user.setRz_Key(admin.getRz_Key());
				user.setRz_Secret(admin.getRz_Secret());
			}else if(action.equals("web")) {
				user.setCompanyName(admin.getCompanyName());
				user.setWebTitle(admin.getWebTitle());
				if (!logo.isEmpty()) {
					if(logo.getContentType().equals("image/jpeg") || logo.getContentType().equals("image/png")) {
						//boolean flag = imageSaver.save(logo, MEDIA_IMAGE_FOLDER_PATH,"logo.png");
						logo.transferTo(new File(MEDIA_IMAGE_FOLDER_PATH +"/logo.png" ));
						user.setLogo(logo.getOriginalFilename());
						model.addAttribute("imageSave", true);
					}else {
						return "redirect:/marketplace/setting?logoInvalidType";
					}
				} 
				if (!favicon.isEmpty()) {
					if(favicon.getContentType().equals("image/jpeg") || favicon.getContentType().equals("image/png")) {
						//boolean flag = imageSaver.save(logo, MEDIA_IMAGE_FOLDER_PATH,"logo.png");
						favicon.transferTo(new File(MEDIA_IMAGE_FOLDER_PATH +"/favicon.png" ));
						model.addAttribute("imageSave", true);
					}else {
						return "redirect:/marketplace/setting?logoInvalidType";
					}
				} 
				
				
			}
			marketplaceAdminRepository.save(user);
			return "redirect:/marketplace/setting?msg";
		
		}catch(Exception e) {
			System.out.println("Error (update user details) : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String viewMarketplaceStatus(Principal p,Model model) {
		// TODO Auto-generated method stub
		try {
			MarketplaceAdmin user=marketplaceAdminRepository.findByEmail(p.getName());
			Map <String,String> map=apiService.getMarketplaceDetails(Map.of("marketplaceEmail",user.getEmail()));
			if(map.get("status").equals("0")) {
				model.addAttribute("marketplaceData", map);
				return "UserPages/marketplaceDetails";
			}else {
				model.addAttribute("errCode",500);
				model.addAttribute("errMsg","Opps, Something went wrong.");
				return "UserPages/error";
			}
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String setApiEndPointInPolicyMaker(Principal p, Model model) {
		try {
			MarketplaceAdmin user=marketplaceAdminRepository.findByEmail(p.getName());
			apiService.verifyMarketplace(Map.of("marketplaceEmail",user.getEmail(),"marketplaceApiUrl",marketplaceApiUrl));
			return "redirect:/marketplace/viewMarketplaceDetails?msg";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}
	
	@Override
	public String changeCodStatus(String status,Model model,Principal p) {
		try {
			logger.info("change cod status : "+status);
			MarketplaceAdmin user=marketplaceAdminRepository.findByEmail(p.getName());
			user.setCodStatus(status);
			marketplaceAdminRepository.save(user);
			return "UserPages/dashboard";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String changeBusinessType(String type,Model model, Principal p) {
		try {
			logger.info("change business type : "+type);
			MarketplaceAdmin user=marketplaceAdminRepository.findByEmail(p.getName());
			user.setBusinessType(type);
			marketplaceAdminRepository.save(user);
			return "success";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return e.getMessage();
		}
	}

	@Override
	public String updateMinimumPurchaseAmt(int amt, Principal p) {
		try {
			logger.info("update minimum purchase amount  : "+amt);
			MarketplaceAdmin user=marketplaceAdminRepository.findByEmail(p.getName());
			user.setMinimumPurchaseAmt(amt);
			marketplaceAdminRepository.save(user);
			return "success";
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	public String updateCodPrepaidPercentage(int amt, Principal p) {
		try {
			logger.info("update cod prepaid percentage  : "+amt);
			if(amt>99){
				throw new Exception("cod prepaid percentage is too high.");
			}
			MarketplaceAdmin user=marketplaceAdminRepository.findByEmail(p.getName());
			user.setCodPrepaidPercentage(amt);
			marketplaceAdminRepository.save(user);
			return "success";
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
