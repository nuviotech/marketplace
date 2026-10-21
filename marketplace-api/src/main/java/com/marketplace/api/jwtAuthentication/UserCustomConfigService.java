package com.marketplace.api.jwtAuthentication;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.marketplace.api.entitys.LoginUser;
import com.marketplace.api.entitys.ReportingAccount;
import com.marketplace.api.entitys.User;
import com.marketplace.api.repository.ReportingAccountRepo;
import com.marketplace.api.repository.UserRepository;


@Service
public class UserCustomConfigService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ReportingAccountRepo reportingAccountRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == null) {
			throw new UsernameNotFoundException("Username is null");
		}
		try {
			String array[] = username.split("#!#");
			String email=array[0];
			String localLoginType = "custom";
			if(array.length==2) {
				localLoginType = array[1];
			}
			
			LoginUser user=(LoginUser)setUserByType(localLoginType, email);
			if(user!=null && localLoginType.equalsIgnoreCase("google")) {
				user.setPassword("dummy");
			}
			if (user == null) {
				throw new UsernameNotFoundException("Invalid User name or password !!");
			} else {
				return new UserCustomConfig(user, localLoginType);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/***
	 * set the user type (Affiliate user,normal user)
	 * */
	private Object setUserByType(String type,String email){
		if(type.equalsIgnoreCase("affiliate_account")) {
			return reportingAccountRepo.findByEmail(email);
		}else if(type.equalsIgnoreCase("normal_account") || type.equalsIgnoreCase("custom")){
			return userRepository.findByEmail(email);
		}else if(type.equalsIgnoreCase("google")){
			return userRepository.findByEmail(email);
		}
		return null;
	}
	
}
