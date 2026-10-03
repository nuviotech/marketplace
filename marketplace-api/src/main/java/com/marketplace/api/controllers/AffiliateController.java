package com.marketplace.api.controllers;

import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.api.entitys.ReferralOrders;
import com.marketplace.api.entitys.ReportingAccount;
import com.marketplace.api.entitys.User;
import com.marketplace.api.jwtHelper.JwtUtil;
import com.marketplace.api.repository.ReferralOrdersRepository;
import com.marketplace.api.repository.ReportingAccountRepo;

@RestController
public class AffiliateController {
	@Autowired
	ReferralOrdersRepository referralOrdersRepository;
	@Autowired
	ReportingAccountRepo reportingAccountRepo;
	@Autowired
	JwtUtil jwtUtil;
	
	Logger logger=LoggerFactory.getLogger(AffiliateController.class);
	
	@GetMapping("/getReferralOrderDetails/{page}")
	public ResponseEntity<?> getReferralOrderDetails(@PathVariable("page") int page,@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
		try {
			ReportingAccount reportingAccount =reportingAccountRepo.findByEmail(jwtUtil.getUsernameFromToken(jwtToken.substring(7)));
			if(reportingAccount!=null) {
				logger.info("referral order accessing by "+reportingAccount.getEmail());
				Pageable pageable=PageRequest.of(page, 20);
				Page<ReferralOrders> pages=referralOrdersRepository.findByAffiliateMarketerId(reportingAccount.getReportingAccountId(),pageable);
				return ResponseEntity.ok(Map.of("status","0","message","done","referralOrders",pages));
			}else {
				logger.warn("Invalid referral order access");
				return ResponseEntity.badRequest().body(Map.of("status","1","message","invalid access!!","referralOrders",null));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("status","-1","message",e.getMessage(),"referralOrders",null));
		}
	}
	
}
