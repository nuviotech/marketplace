package marketplace.admin.api.marketplace_Admin.configuration;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import marketplace.admin.api.marketplace_Admin.enititys.MarketplaceAdmin;
import marketplace.admin.api.marketplace_Admin.repository.MarketplaceAdminRepository;



public class UserCustomConfigService implements UserDetailsService {
	@Autowired
	MarketplaceAdminRepository MarketplaceAdminRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		try {
			MarketplaceAdmin user = MarketplaceAdminRepository.findByEmail(username);
			if (user == null) {
				throw new UsernameNotFoundException("Invalid User name or password !!");
			} else {
				return new UserCustomConfig(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return null;
	}

}
