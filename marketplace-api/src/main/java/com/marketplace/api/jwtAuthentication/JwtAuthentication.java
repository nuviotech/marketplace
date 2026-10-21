package com.marketplace.api.jwtAuthentication;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthentication implements AuthenticationEntryPoint {

	Logger logger=LoggerFactory.getLogger(JwtAuthentication.class);
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		// TODO Auto-generated method stub
		try {
		logger.warn("ERROR (code : 401, msg : token expire) : "+authException.getMessage());
		response.sendError(401,"Unauthorized "+authException.getMessage());
		authException.getMessage();
		}catch(Exception e) {
			logger.error("ERROR : "+e.getMessage());
		}
	}

}
