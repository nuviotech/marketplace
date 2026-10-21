package com.marketplace.api.jwtAuthentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class UserConfiguration {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthentication jwtAuthentication;

	public UserConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthentication jwtAuthentication) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.jwtAuthentication = jwtAuthentication;
	}

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserCustomConfigService();
	}

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setUserDetailsService(getUserDetailsService());
		dao.setPasswordEncoder(getBCryptPasswordEncoder());
		return dao;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.cors(cors -> {})
			.authenticationProvider(getAuthenticationProvider())
			.authorizeHttpRequests(authorize -> authorize
			.requestMatchers("/login", "/googleLogin", "/saveUser", "/forgotePasswordCreateId", "/resetPassword", "/").permitAll()

			//*************************************************
			//allowed endpoint enter here
			//.antMatchers("/getOrderDetails").permitAll()
			.requestMatchers("/getTotalCountOfOrders", "/getReturnDetails", "/getTotalCountOfReturnDetails", "/refundOrder", "/getAllProducts").permitAll()
		//	.antMatchers("/products").permitAll()
			//.antMatchers("/products/").permitAll()
			.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.requestMatchers("/getActiveSellers", "/products/**", "/products", "/product-categories", "/brands", "/products/count", "/productsByIds", "/searchRecords", "/saveSubscriberEmail", "/getRelatedProduct", "/getOrderDetailsInJson/*", "/changeSellerStatus", "/saveSellerRequest", "/getReportingAccountDetails", "/saveAffilateAccountDatails", "/updateProductCache", "/saveReportAbuseData", "/applyCoupon", "/getProductVariations", "/getDefaultCoupon", "/updateOrderProductStatus", "/getPolicy/*").permitAll()
			//*************************************************
			.requestMatchers(HttpMethod.OPTIONS).permitAll()
			.requestMatchers("/razorpayWebhook").permitAll()
			.anyRequest().authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthentication));
		http.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
