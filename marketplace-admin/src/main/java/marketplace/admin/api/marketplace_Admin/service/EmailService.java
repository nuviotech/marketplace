package marketplace.admin.api.marketplace_Admin.service;

import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

public interface EmailService {
	public String getEmailDetails(int page,int limit,int msgCode, Model model);
	public String saveEmailConfigDetails(Map<String,String>map,Model model);
	public String saveEmailDetails(Map<String,String>map,Model model);
	public String sendEmail(int emailId,Model model);
	
	//this is for message template
	public String showEmailConfigurationForm(int msgCode,Model model);
	public String updateEmail(int emailId, Model model);
	public String removeEmail(int emailId, Model model);
}
