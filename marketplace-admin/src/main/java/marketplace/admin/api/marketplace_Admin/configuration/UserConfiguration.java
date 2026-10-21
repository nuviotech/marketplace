package marketplace.admin.api.marketplace_Admin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserConfiguration {

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
		http.authenticationProvider(getAuthenticationProvider())
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/marketplace/**").hasRole("MARKETPLACE")
				.requestMatchers("/**").permitAll())
			.formLogin(form -> form.loginPage("/")
				.loginProcessingUrl("/do_login")
				.defaultSuccessUrl("/marketplace/dashboard"))
			.cors(cors -> {})
			.csrf(csrf -> csrf.disable());
		return http.build();
	}
}
