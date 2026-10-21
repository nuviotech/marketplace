package marketplace.admin.api.marketplace_Admin.Implementation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.Seller;
import marketplace.admin.api.marketplace_Admin.enititys.Settlement;
import marketplace.admin.api.marketplace_Admin.repository.SellerRepository;
import marketplace.admin.api.marketplace_Admin.repository.SettlementRepository;
import marketplace.admin.api.marketplace_Admin.service.SettlementService;
import marketplace.admin.api.marketplace_Admin.util.ExcelService;

@Service
public class SettlementServiceImpl implements SettlementService {
	@Autowired
	SettlementRepository settlementRepository;
	@Autowired
	ExcelService excelService;
	@Autowired
	SellerRepository sellerRepository;
	
	Logger logger=LoggerFactory.getLogger(SettlementServiceImpl.class);
	
	@Override
	public String getSettlementDetails(int page, String startDate, String endDate, Model model) {
		try {
			Pageable pageable = PageRequest.of(page, 20, Sort.by("createdDtTime").descending());
			if (startDate.equals("") || endDate.equals("") || startDate.length() == 0) {
				
				Page<Settlement> pages = settlementRepository.findAll(pageable);
				List<Settlement> list = new ArrayList<>();

				for(Settlement s : pages.getContent()) {
				    Seller seller = sellerRepository.findByRegisteredSellerId(s.getSellerId());
				    if(seller!=null) {
				    	s.setSellerEmail(seller.getEmailAddr());
				    	list.add(s);
					}else{
						logger.warn("Seller not present in settlement id "+s.getSettlementId());
					}
				}

				
				model.addAttribute("totalPage", pages.getTotalPages());
				model.addAttribute("currentPage", page);
				model.addAttribute("settlements", list);
				model.addAttribute("startDate", "");
				model.addAttribute("endDate", "");
			} else {
				// convert string dates into date
				Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
				Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
				// get here thoes settlement data whose in between in startdate and enddate

				Page<Settlement> pages = settlementRepository.findByCreatedDtTimeBetween(fromDate, toDate, pageable);
				model.addAttribute("totalPage", pages.getTotalPages());
				model.addAttribute("currentPage", page);
				model.addAttribute("settlements", pages);
				model.addAttribute("startDate", startDate);
				model.addAttribute("endDate", endDate);
			}

			return "UserPages/settlementTable";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errCode", 400);
			model.addAttribute("errMsg", e.getMessage());
			return "UserPages/error";
		}
	}

	@Override
	public void downloadExcelFile(String startDate, String endDate, HttpServletResponse response) {
		Workbook workbook=null;
		try {
			List<Settlement> settlementData = null;
			String filename = "Settlement_report_";
			if (startDate.equals("") || endDate.equals("") || startDate.length() == 0) {
				settlementData = settlementRepository.findAll();
				filename += new Date() + ".xlsx";
			} else {
				Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
				Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
				settlementData = settlementRepository.findByCreatedDtTimeBetween(fromDate, toDate);
				filename += "sd-" + startDate + "_ed-" + endDate + ".xlsx";
			}
			workbook = excelService.generateSettlementExcelFile(settlementData);
			
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="+filename);
			workbook.write(response.getOutputStream());
			workbook.close();
		} catch (Exception e) {
			logger.error("ERROR : "+e.getMessage());
			e.printStackTrace();
		}finally {
			// Close the workbook if it's not null
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error("ERROR INSIDE FINALLY : "+e.getMessage());
                    e.printStackTrace();
                }
            }
		}
	}
	
	
}
