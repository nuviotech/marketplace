package marketplace.admin.api.marketplace_Admin.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import marketplace.admin.api.marketplace_Admin.enititys.User;



public interface UserRepository extends JpaRepository<User,Integer> {

	public User findByEmail(String userEmail);
}
