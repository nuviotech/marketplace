package com.marketplace.api.jwtAuthentication;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.marketplace.api.entitys.LoginUser;
import com.marketplace.api.entitys.User;


public class UserCustomConfig implements UserDetails {
	@Autowired
	private UserCustomConfigService userCustomConfigService;
	private String action="";
	private LoginUser user;

	public UserCustomConfig(LoginUser u,String action) {
		super();
		this.user = u;
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		HashSet<SimpleGrantedAuthority> set = new HashSet<SimpleGrantedAuthority>();
		set.add(new SimpleGrantedAuthority(user.getRole()));
		return set;
	}
	
	
	@Override
	public String getPassword() {
		
		// TODO Auto-generated method stub
		if(action.equalsIgnoreCase("google")) {
			return "$2a$10$Om3aDbEyEO99mUCD69O1e.SG42GkpNa/FYNz23pLbQnEv.gd1lyOy";
		}else {
			return user.getPassword();
		}
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
