package com.marketplace.api.jwtAuthentication;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import com.marketplace.api.jwtHelper.JwtUtil;



@Component
@CrossOrigin
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserCustomConfigService userCustomConfigService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("Filtering......");
		String fullToken = request.getHeader("Authorization");
		String userName=null;
		String token=null;
		//System.out.println("Token : "+fullToken);
		//token is empty or not and check the token format here...
		if(fullToken!=null && fullToken.startsWith("Bearer ")) {
			//(bearer abcdefghijk) so actual token start from 7 index 
			token=fullToken.substring(7);
			try {
				//get user name and loginType from token
				userName=this.jwtUtil.getUsernameFromToken(token);
				String loginType=this.jwtUtil.getLoginTypeFromToken(token);
				if (userName != null && loginType != null && !loginType.isEmpty() && !userName.contains("#!#")) {
					userName = userName + "#!#" + loginType;
				}
			}catch(Exception e) {
				System.out.println("(filtering...) error message : "+e.getMessage());
				
				e.printStackTrace();
			}
			
			UserDetails userDetails=this.userCustomConfigService.loadUserByUsername(userName);
			//security work here
			if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			
			}else {
				throw new Error("Token is not validate");
			//	System.out.println("token is not valideted..");
			}
		}
		//response.setHeader("Access-Control-Allow-Origin","*");
	
		filterChain.doFilter(request, response);
	}
	
}
