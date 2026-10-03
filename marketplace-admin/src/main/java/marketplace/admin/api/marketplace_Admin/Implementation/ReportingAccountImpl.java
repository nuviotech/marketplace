package marketplace.admin.api.marketplace_Admin.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.ReferralOrders;
import marketplace.admin.api.marketplace_Admin.enititys.ReportingAccount;
import marketplace.admin.api.marketplace_Admin.repository.ReferralOrderRepository;
import marketplace.admin.api.marketplace_Admin.repository.ReportingAccountRepo;
import marketplace.admin.api.marketplace_Admin.service.ReportingAccountService;

@Service
public class ReportingAccountImpl implements ReportingAccountService {
	@Autowired
	ReportingAccountRepo reportingAccountRepo;
	@Autowired
	ReferralOrderRepository referralOrderRepository;
	
	Logger logger = LoggerFactory.getLogger(ReportingAccountImpl.class);
	
	public Model getData(int page,Model model) {
		Pageable pageable=PageRequest.of(page, 10, Sort.by("updatedDtTime").descending());
		Page<ReportingAccount> reportingAccounts=reportingAccountRepo.findAll(pageable);
		model.addAttribute("totalPage", reportingAccounts.getTotalPages());
		model.addAttribute("currentPage", page);
		model.addAttribute("reportingAcnts",reportingAccounts);
		return model;
	}
	
	@Override
	public String getReportingAccountDetails(int page, Model model) {
		// TODO Auto-generated method stub
		getData(page, model);
		return "UserPages/reportingAccountTable";
	}

	@Override
	public String saveReportingAccountDetails(ReportingAccount reportingAccount, Model model) {
		// TODO Auto-generated method stub
		try {
			
			reportingAccountRepo.save(reportingAccount);
			getData(0, model);
			model.addAttribute("message","record save success fully");
			return "UserPages/reportingAccountTable";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode",HttpStatus.INTERNAL_SERVER_ERROR);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String getReportingAccountForm(int reportingAccntId,Model model) {
		// TODO Auto-generated method stub
		ReportingAccount ra=null;
		if(reportingAccntId!=0) {
			ra =reportingAccountRepo.findByReportingAccountId(reportingAccntId);
			model.addAttribute("tagName", "Update");
		}
		if(reportingAccntId==0 || ra==null) {
			ra=new ReportingAccount();
			model.addAttribute("tagName", "Add");
		}
		
		model.addAttribute("reportAcnt",ra);
		return "UserPages/reportingAccountForm";
	}

	@Override
	public String getReferralOrders(int reportingAccntId, int page, Model model) {
		try {
			Pageable pageable = PageRequest.of(page, 20);  
			Page<ReferralOrders> data = referralOrderRepository.findByAffiliateMarketerId(reportingAccntId,pageable);
			model.addAttribute("totalPage", data.getTotalPages());
			model.addAttribute("currentPage", page);
			model.addAttribute("referralOrders",data);
			return "UserPages/referralOrderTable";
		}catch(Exception e) {
			logger.error("-getReferralOrders- ERROR : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",HttpStatus.INTERNAL_SERVER_ERROR);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public String referralOrderSetAsDistributed(int referralId) {
		try {
			ReferralOrders ro= referralOrderRepository.findByReferralOrderId(referralId);
			if(ro!=null) {
				logger.info("referral order payment setlement is changed N => Y id is "+referralId);
				ro.setPaymentSettlement("Y");
				referralOrderRepository.save(ro);
				return "0";
			}else {
				logger.warn("INVALID REFERRAL ORDER ID (NON_EXIST) : "+referralId);
				return "1";
			}
		}catch(Exception e) {
			logger.error("- referralOrderSetAsDistributed - ERROR : "+e.getMessage());
			e.printStackTrace();
			return "-1";
		}
	}
	
}
