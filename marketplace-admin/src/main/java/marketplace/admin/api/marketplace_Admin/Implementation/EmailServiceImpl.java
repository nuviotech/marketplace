package marketplace.admin.api.marketplace_Admin.Implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.EmailConfig;
import marketplace.admin.api.marketplace_Admin.enititys.Emails;
import marketplace.admin.api.marketplace_Admin.enititys.User;
import marketplace.admin.api.marketplace_Admin.repository.EmailConfigRepository;
import marketplace.admin.api.marketplace_Admin.repository.EmailRepository;
import marketplace.admin.api.marketplace_Admin.repository.UserRepository;
import marketplace.admin.api.marketplace_Admin.service.EmailActions;
import marketplace.admin.api.marketplace_Admin.service.EmailService;


@Service
public class EmailServiceImpl implements EmailService  {
	@Autowired
	EmailConfigRepository emailConfigRepository;
	@Autowired
	EmailRepository emailRepository;
	@Autowired
	EmailActions emailActions;
	@Autowired
	UserRepository userRepository;
	
	Logger logger=LoggerFactory.getLogger(EmailServiceImpl.class);
	
	public String getEmailDetails(int page,int limit,int msgCode, Model model) {
		try {
			Pageable pageable = PageRequest.of(page,limit,Sort.by("createdDateAndTime").descending());
			Page<Emails> emails = emailRepository.findAll(pageable);
			model.addAttribute("emails", emails);
			model.addAttribute("currentPage", page);
			
			model.addAttribute("alert", msgCode==0? "success":msgCode==1?"sendEmail":"none");
			model.addAttribute("totalPage",emails.getTotalPages());
			
			return "UserPages/emailTable";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String saveEmailConfigDetails(Map<String, String> map, Model model) {
		try {
			EmailConfig emailConfig=null;
			emailConfig=emailConfigRepository.findByType(map.get("type"));
			if(emailConfig==null) {
				emailConfig=new EmailConfig();
			}
			emailConfig.setSenderName(map.get("senderName"));
			emailConfig.setSenderEmail(map.get("email"));
			emailConfig.setPassword(map.get("password"));
			emailConfig.setType(map.get("type"));
			emailConfigRepository.save(emailConfig);
			model.addAttribute("alert", "success");
			System.out.println("done");
			return "redirect:/marketplace/emailConfig?msgCode=0";
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String saveEmailDetails(Map<String, String> map, Model model) {
		try {
			
			Emails email=null;
			if(map.get("action").equals("UPDATE"))
				email=emailRepository.findByEmailId(Integer.parseInt(map.get("emailId")));
			else
				email=new Emails();
			email.setCreatedDateAndTime(new Date());
		 	email.setTotalRecipient(Integer.parseInt(map.get("totalRecipient")));
			email.setSubject(map.get("subject"));
			email.setEmailMsg(map.get("emailMsg").replaceAll("\r\n",""));
			email.setSendeEmailType(map.get("senderEmail"));
			email.setEmailType(map.get("receiverUserEmailType"));
			if(map.get("receiverUserEmailType").equals("CUSTOM")) {
				email.setReceiverEmails(map.get("reciverEmails"));
			}else {
				email.setReceiverEmails(null);
			}
			emailRepository.save(email);
			return "redirect:/marketplace/emailTemplates?msgCode=0&page=0";
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String sendEmail(int emailId, Model model) {
		try {
    		Emails email=emailRepository.findByEmailId(emailId);
    		if(email!=null) {
	    		StringBuilder strEmails=new StringBuilder();
				if(email.getEmailType().equals("CUSTOM")) {
					List<String> grpEmails = groupEmails(email.getReceiverEmails().split(","),email.getTotalRecipient());
					for(String emailRecivers:grpEmails) {
						emailActions.sendCustomEmailToUsers(emailRecivers, email);
					}
				}else if(email.getEmailType().equals("ALL_USERS")) {
					List<User> users=userRepository.findAll();
					for(User u:users) {
						strEmails.append(u.getEmail()+",");
					}
					List<String> grpEmails = groupEmails(strEmails.toString().split(","),email.getTotalRecipient());
					for(String emailRecivers:grpEmails) {
						emailActions.sendCustomEmailToUsers(emailRecivers, email);
					}
				}
    		}else {
    			return "invalid email !!";
    		}
			return "done";
		}catch(Exception e) {
			logger.error("email not send!!");
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	public String showEmailConfigurationForm(int msgCode, Model model) {
		try {
			System.out.println(msgCode);
			EmailConfig config=new EmailConfig();
			EmailConfig generalEmailConfig=emailConfigRepository.findByType("GENERAL");
			EmailConfig salesRepresentativeEmailConfig=emailConfigRepository.findByType("SALES_REPRENSENTATIVE");
			EmailConfig supportEmailConfig=emailConfigRepository.findByType("SUPPORT");
			EmailConfig custom1EmailConfig=emailConfigRepository.findByType("CUSTOM_ONE");
			EmailConfig custom2EmailConfig=emailConfigRepository.findByType("CUSTOM_TWO");

			
			model.addAttribute("alert", msgCode==0?"success":"none");
			model.addAttribute("generalEmailConfig",generalEmailConfig!=null?generalEmailConfig:config);
			model.addAttribute("salesRepresentativeEmailConfig",salesRepresentativeEmailConfig!=null?salesRepresentativeEmailConfig:config);
			model.addAttribute("supportEmailConfig",supportEmailConfig!=null?supportEmailConfig:config);
			model.addAttribute("custom1EmailConfig",custom1EmailConfig!=null?custom1EmailConfig:config);
			model.addAttribute("custom2EmailConfig",custom2EmailConfig!=null?custom2EmailConfig:config);
			return "UserPages/emailConfiguration";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String updateEmail(int emailId, Model model) {
		try {
			Emails email = emailRepository.findByEmailId(emailId);
			if(email==null)
				throw new Exception("email not found by id "+emailId);
			model.addAttribute("email", email);
			return "UserPages/addEmail";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
			
		}
	}

	@Override
	public String removeEmail(int emailId, Model model) {
		try {
			Emails email =emailRepository.findByEmailId(emailId);
			if(email!=null) {
				emailRepository.delete(email);
				return "deleted";
			}else {
				return "email not exist!!";
			}
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";		}
	}
	
	public static List<String> groupEmails(String[] names, int groupSize) {
        List<String> emailGroups = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < names.length; i++) {
            sb.append(names[i]);
            // Append comma if it's not the last name in the group
            if ((i + 1) % groupSize != 0 && i != names.length - 1) {
                sb.append(",");
            }
            // Check if we've added enough names for a group
            if ((i + 1) % groupSize == 0 || i == names.length - 1) {
                // Add the current group to the list
                emailGroups.add(sb.toString());
                // Reset the StringBuilder for the next group
                sb = new StringBuilder();
            }
        }
        return emailGroups;
    }


}
