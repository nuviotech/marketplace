package marketplace.admin.api.marketplace_Admin.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import marketplace.admin.api.marketplace_Admin.enititys.Coupon;
import marketplace.admin.api.marketplace_Admin.enititys.Emails;
import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.enititys.Policys;
import marketplace.admin.api.marketplace_Admin.enititys.ReportingAccount;
import marketplace.admin.api.marketplace_Admin.repository.CouponRepository;
import marketplace.admin.api.marketplace_Admin.repository.MarketplaceAdminRepository;
import marketplace.admin.api.marketplace_Admin.repository.MediaImagesRepository;
import marketplace.admin.api.marketplace_Admin.service.AdminService;
import marketplace.admin.api.marketplace_Admin.service.CategoriesCommissionService;
import marketplace.admin.api.marketplace_Admin.service.CouponService;
import marketplace.admin.api.marketplace_Admin.service.EmailService;
import marketplace.admin.api.marketplace_Admin.service.ImageSaver;
import marketplace.admin.api.marketplace_Admin.service.OrderService;
import marketplace.admin.api.marketplace_Admin.service.PolicyService;
import marketplace.admin.api.marketplace_Admin.service.ReportingAccountService;
import marketplace.admin.api.marketplace_Admin.service.SellerService;
import marketplace.admin.api.marketplace_Admin.service.SettlementService;
import marketplace.admin.api.marketplace_Admin.service.UserService;


@Controller
@RequestMapping("/marketplace")
@CrossOrigin
public class MarketplaceController {
	@Autowired
	MarketplaceAdminRepository marketplaceAdminRepository;
	@Autowired
	SellerService sellerService;
	@Autowired
	OrderService orderService;
	@Autowired
	CategoriesCommissionService categoriesCommissionService;
	@Autowired
	AdminService adminService;
	@Autowired
	CouponService couponService;
	@Autowired 
	EmailService emailService;
	@Autowired
	SettlementService settlementService;
	@Autowired
	ReportingAccountService reportingAccountService;
	@Autowired
	MediaImagesRepository mediaImagesRepository;
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	UserService usersService;
	@Autowired
	ImageSaver imageSaver;
	@Autowired
	PolicyService policyService;
	
	@Value("${marketplace.api.url}")
	String marketplaceApiUrl;
	
	@Value("${manageEcom.api.url}")
	String manageEcomApiUrl;
	
	@Value("${apiKey}")
	String apiKey;
	
	@Value("${policyMaker.url}")
	private String baseUrl;
	MarketplaceAdmin user=null;
	
	
	@ModelAttribute
	public void setDefaultModel(Principal p,Model model) {
		user = marketplaceAdminRepository.findByEmail(p.getName());
		if(user.getSupportEmailId()!=null && user.getSupportEmailId().equals("nuviosellers.2024@gmail.com")) {
			user.setSupportEmailId("");
			user.setSupportEmailPassword("");
		}
		model.addAttribute("user",user);
		model.addAttribute("title", "Marketplace Admin || welcome " );
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model){
	//	model.addAttribute("user",new User());
		try {
		return "UserPages/dashboard";
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@GetMapping("/allSellers")
	public String getAllSellers(@RequestParam("page") int page,@RequestParam(name ="searchKey",required = false,defaultValue = "") String searchKey,@RequestParam(name ="searchValue",required = false,defaultValue = "") String searchValue ,Model model) {
		return sellerService.getAllSellers(page,searchKey,searchValue,model);
	}
	
	@GetMapping("/getSeller")
	public String getActiveSeller(Model model,@RequestParam("status") String status,@RequestParam("page") int page,@RequestParam(name="limit",required = false, defaultValue = "10") int limit){
		return sellerService.getActiveSeller(model, status, page, limit);
	}
	
	@GetMapping("/activeTheSeller")
	public String activeTheSeller(@RequestParam("id")String id,Model model,Principal p) {
		return sellerService.activeTheSeller(id, model, p, user);
	}
	
	@GetMapping("/deactiveTheSeller")
	public String deactivateTheSeller(@RequestParam("id")String id,Principal p,Model model) {
		return sellerService.deactivateTheSeller(id, p, model);
	}
	
	@GetMapping("/getSellerProducts/{sellerId}/{totalProductCount}")
	public String getSellerProducts(@PathVariable("sellerId") String sellerId,@PathVariable("totalProductCount")int totalProductCount,@RequestParam("page") int page ,@RequestParam(name="limit",defaultValue = "20",required = false) int limit,Model model) {
		return sellerService.getSellerProducts(sellerId, totalProductCount, page, limit, model);
	}
	
	@GetMapping("/editSellerMOV")
	@ResponseBody
	public String editSellerMOV(@RequestParam("sellerEmail") String sellerEmail,@RequestParam("mov") int mov ) {
		return sellerService.editSellerMOV(sellerEmail,mov);
	}
	
	@GetMapping("/orderDetails")
	public String getAllOrderDetails(@RequestParam("page") int page,@RequestParam("action") String action,Model model) {
		return orderService.getAllOrderDetails(page, action, model);
	}

	@PostMapping("/updateOrderStatus")
	public String updateOrderStatus(@RequestParam("orderProductId") String orderProductId,
	                                 @RequestParam("status") String status,
	                                 @RequestParam(name = "page", defaultValue = "0") int page,
	                                 @RequestParam(name = "action", defaultValue = "all") String action) {
		orderService.updateOrderStatus(orderProductId, status);
		return "redirect:/marketplace/orderDetails?page=" + page + "&action=" + action;
	}

	@PostMapping("/bulkUpdateOrderStatus")
	public String bulkUpdateOrderStatus(@RequestParam(name = "orderProductIds", required = false) List<String> orderProductIds,
	                                     @RequestParam("status") String status,
	                                     @RequestParam(name = "page", defaultValue = "0") int page,
	                                     @RequestParam(name = "action", defaultValue = "all") String action) {
		if (orderProductIds != null && !orderProductIds.isEmpty()) {
			orderService.bulkUpdateOrderStatus(orderProductIds, status);
		}
		return "redirect:/marketplace/orderDetails?page=" + page + "&action=" + action;
	}
	
	@GetMapping("/returnOrderDetails")
	public String getAllrefundOrderDetails(@RequestParam("page") int page,@RequestParam("action") String action,Model model) {
		return orderService.getAllrefundOrderDetails(page, action, model);
	}
		
	//refund order details
	@GetMapping("/refundOrder")
	public String refundOrder(@RequestParam("id")int id,Model model) {
		return orderService.refundOrder(id, model, user);
	}
	
	/*************************************************************************/
	
	/*
	 * categories commission
	 * 
	 * */
	@GetMapping("/categories_commission")
	public String categoriesCommission(Model model) {
		return categoriesCommissionService.getCategoriesCommission(model);
	}
	
	@GetMapping("/editCategoriesCommission")
	@ResponseBody
	public String editcategoriesCommission(@RequestParam("commission") int commission,@RequestParam("catId") String catId ) {
		return categoriesCommissionService.editcategoriesCommission(commission, catId);
	}
	
	/**************************************************************************************/
	
	/**
	 *  profile update
	 * 
	 * */
	
	@GetMapping("/update_profile")
	public String updateProfilePage(Model model) {
		model.addAttribute("title","Marketplace admin || update profile page");
		model.addAttribute("updateUserPData", false);
		return "UserPages/profile";
	}
	
	@GetMapping("/setting")
	public String settingPage(Model model,Principal p) {
		try {
			model.addAttribute("title","Marketplace admin || setting page");
			MarketplaceAdmin mAdmin=marketplaceAdminRepository.findByEmail(p.getName());

			Coupon c=couponRepository.findByIsDefault("yes");
			if(c==null) {
				model.addAttribute("defaultCoupon", "");
			}else {
				model.addAttribute("defaultCoupon", c.getCouponCode());
			}
			
			model.addAttribute("businessType", mAdmin.getBusinessType());
			model.addAttribute("minimumPurchaseAmt",mAdmin.getMinimumPurchaseAmt());
			model.addAttribute("codPrepaidPercentage", mAdmin.getCodPrepaidPercentage());
			return "UserPages/setting";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}
	
	@PostMapping("/saveUserDetails")
	public String updateTheUserDetails(@ModelAttribute MarketplaceAdmin admin,Model model,Principal p) {
		return adminService.updateTheUserDetails(admin, model, p);	
	}
	
	@PostMapping("/{action}/saveSettings")
	public String saveAdminSetting(@PathVariable("action") String action,@ModelAttribute MarketplaceAdmin admin,Model model,@RequestParam(required = false, name = "webLogo" ) MultipartFile logo,@RequestParam(required = false, name = "webFavicon" ) MultipartFile favicon,Principal p) {
		return adminService.saveAdminSetting(action, admin, model, logo,favicon, p);
	}
	
	//coupon 
	@GetMapping("/coupons")
	private String couponsTableShow(@RequestParam("page") int page,@RequestParam(name="limit", required=false,defaultValue = "10") int limit,@RequestParam(name="msgCode",defaultValue = "-1",required = false) int msg, Model model) {
		return couponService.getCouponDetails(page, limit, msg, model);
	}
	
	@GetMapping("/addCouponForm")
	private String showAddCouponForm(Model model) {
		try {
			model.addAttribute("coupon",new Coupon());
			model.addAttribute("alert","none");
			return "UserPages/couponForm";
		}catch(Exception e) {
			System.out.println("Error (update user details) : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}
	
	@PostMapping("/saveCouponDetails")
	public String saveCouponDetails(@RequestParam Map<String,Object> map,Model model) {
		return couponService.saveCouponDetails(map, model);
	}
	
	@GetMapping("/defaultCoupon/{opration}/{couponCode}")
	@ResponseBody
	public String changeDefaultCoupon(@PathVariable("opration") String operation,@PathVariable("couponCode") String couponCode) {
		System.out.println(couponCode);
		return couponService.manageDefaultCoupon(operation,couponCode);
	}
	
	@GetMapping("/changeCouponStatus/{page}/{couponId}/{status}")
	public String changeCouponStatus(@PathVariable("couponId") int couponId,@PathVariable("page") int page,@PathVariable("status") String status,Model model) {
		return couponService.changeCouponStatus(couponId,page,status,model);
	}
	
	//email table show
	
	@GetMapping("/emailTemplates")
	public String showAllEmailTemmplate(@RequestParam("page") int page,@RequestParam(name="limit",required = false,defaultValue = "10") int limit,@RequestParam(name="msgCode",required = false,defaultValue = "-1") int msgCode, Model model) {
		return emailService.getEmailDetails(page, limit, msgCode, model);
	}
	
	@GetMapping("/emailConfig")
	public String showEmailConfigurationForm(@RequestParam(name="msgCode",required = false,defaultValue = "-1") int msgCode,Model model) {
		return emailService.showEmailConfigurationForm(msgCode, model);
	}
	
	@PostMapping("/saveEmailConfigDetails")
	public String saveEmailConfigDetails(@RequestParam Map<String,String>map,Model model) {
		return emailService.saveEmailConfigDetails(map, model);
	}
	
	@GetMapping("/addEmail")
	public String saveNewEmailTemplateForm(Model model) {
		try {
			model.addAttribute("email", new Emails());
			return "UserPages/addEmail";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}
	
	@GetMapping("/updateEmailTemplate/{emailId}")
	public String updateEmailTemplateForm(@PathVariable("emailId") int emailId,Model model) {
		return emailService.updateEmail(emailId,model);
	}
	
	
	@GetMapping("/removeEmailTemplate/{emailId}")
	@ResponseBody
	public String removeEmailTemplateForm(@PathVariable("emailId") int emailId,Model model) {
		return emailService.removeEmail(emailId,model);
	}
	
	@PostMapping("/saveEmailBodyDetails")
	public String saveEmailDetails(@RequestParam Map<String,String>map,Model model) {
		return emailService.saveEmailDetails(map, model);
	}
	
	@GetMapping("/sendEmailOnClick")
	@ResponseBody
	public String sendEmailOnClick(@RequestParam("emailId") int emailId,Model model) {
		return emailService.sendEmail(emailId, model);
	}
	
	//settlement calls
	@GetMapping("/settlement")
	public String getSettlements(@RequestParam("page") int page,@RequestParam(name="startDate" , defaultValue = "", required = false) String startDate,@RequestParam(name="endDate" , defaultValue = "", required = false) String endDate ,Model model) {
		return settlementService.getSettlementDetails(page, startDate, endDate, model);
	}
	
	//reporting account call
	@GetMapping("/reportingAccountDetails")
	public String getReportingAccountDetailsPage(@RequestParam("page") int page,Model model) {
		return reportingAccountService.getReportingAccountDetails(page, model);
	}
	
	//reporting account call
	@GetMapping("/reportingAccountDetailsForm")
	public String getReportingAccountForm(@RequestParam(name = "reporting_accnt_id",required = false,defaultValue = "0") int reportingAccntId,Model model) {
		return reportingAccountService.getReportingAccountForm(reportingAccntId,model);
	}
	
	@PostMapping("/saveReportingAccountDetails")
	public String saveReportingAccountDetails(@ModelAttribute ReportingAccount reportingAccount,Model model) {
		return reportingAccountService.saveReportingAccountDetails(reportingAccount, model);
	}
	
	//marketplace status
	@GetMapping("/viewMarketplaceDetails")
	public String viewMarketplaceDetails(Principal p,Model model) {
		return adminService.viewMarketplaceStatus(p, model);
	}
	
	@GetMapping("/setMarketplaceApiEndpoint")
	public String setMarketplaceApiEndpointInPolicyMaker(Principal p,Model model) {
		return adminService.setApiEndPointInPolicyMaker(p, model);
	}
	
	@GetMapping("/changeCodStatus/{status}")
	private String changeCODStatus(@PathVariable("status") String status,Model model,Principal p) {
		return adminService.changeCodStatus(status, model, p);
	}
	
	@GetMapping("/changeBusinessType/{type}")
	@ResponseBody
	private String changeBusinessType(@PathVariable("type") String type,Model model,Principal p) {
		return adminService.changeBusinessType(type,model, p);
	}
	
	@GetMapping("/updateMinimumPurchaseAmt")
	@ResponseBody
	private String updateMinimumPurchaseAmt(@RequestParam("amt") int amt,Principal p) {
		return adminService.updateMinimumPurchaseAmt(amt, p);
	}
	
	@GetMapping("/updateCodPrepaidPercentage")
	@ResponseBody
	private String updateCodPrepaidPercentage(@RequestParam("percentage") int amt,Principal p) {
		return adminService.updateCodPrepaidPercentage(amt, p);
	}
	
	@GetMapping("/getUsers")
	private String getRegisteredUser(@RequestParam("page") int page,@RequestParam(name = "limit", required = false, defaultValue = "10") int limit, Model model) {
		return usersService.getUserDetails(page, limit, model);
	}
	
	@GetMapping("/mediaFiles")
	public String mediaFiles(@RequestParam("page") int page, @RequestParam(name="limit",defaultValue = "10", required = false) int limit,Model model) {
		return imageSaver.getMediaImages(page, limit, model);
	}
	
	@GetMapping("/removeMediaFile/{id}")
	@ResponseBody
	public String removeMediaFile(@PathVariable("id") int id) {
		return imageSaver.removeMediaFile(id);
	}
	
	@PostMapping("/uploadMediaImages")
	public String handleFileUpload(@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttributes) {
       return  imageSaver.handleMediaImageUpload(files, redirectAttributes);
    }
	
	@GetMapping("/download/excel")
    public void downloadExcel(@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,HttpServletResponse response) {
		settlementService.downloadExcelFile(startDate, endDate, response);
	}
	
	@GetMapping("/editPolicyPage/{type}")
	public String getEditPolicyTemplate(@PathVariable("type") String type,Model model) {
		return policyService.getEditPolicyTemplate(type, model);
	}
	
	@PostMapping("/savePolicyDetails")
	public String savePolicyDetails(@ModelAttribute Policys policy,Model model) {
		return policyService.savePolicyDetails(policy,model);
	}  
	
	//afiliateMarkete == reportingAccount
	@GetMapping("/getReferralOrders/{affiliateMarketerId}")
	public String getReferralOrders(@PathVariable("affiliateMarketerId") int affiliateMarketerId,@RequestParam(name="page",required = false,defaultValue = "0") int page,Model model) {
		return reportingAccountService.getReferralOrders(affiliateMarketerId, page, model);
	}
	
	@GetMapping("/makeReferalOrderAsDistributed/{refferalOrderId}")
	@ResponseBody
	public String makeReferalOrderAsDistributed(@PathVariable("refferalOrderId") int refferalOrderId) {
		return reportingAccountService.referralOrderSetAsDistributed(refferalOrderId);
	}
}
