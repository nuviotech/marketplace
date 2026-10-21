package marketplace.admin.api.marketplace_Admin.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import marketplace.admin.api.marketplace_Admin.enititys.Emails;
import marketplace.admin.api.marketplace_Admin.enititys.User;
import marketplace.admin.api.marketplace_Admin.repository.UserRepository;
import marketplace.admin.api.marketplace_Admin.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public String getUserDetails(int page, int limit, Model model) {
		try {
			Pageable pageable = PageRequest.of(page,limit,Sort.by("createdDtTime").descending());
			Page<User> users = userRepository.findAll(pageable);
			model.addAttribute("users", users);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPage",users.getTotalPages());
			return "UserPages/userTable";
		}catch(Exception e) {
			logger.error("ERROR : "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("errCode",500);
			model.addAttribute("errMsg",e.getMessage());
			return "UserPages/error";
		}
	}

}
