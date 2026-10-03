package marketplace.admin.api.marketplace_Admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import marketplace.admin.api.marketplace_Admin.enititys.EmailConfig;

@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Integer> {
	public EmailConfig findByType(String type);
}
