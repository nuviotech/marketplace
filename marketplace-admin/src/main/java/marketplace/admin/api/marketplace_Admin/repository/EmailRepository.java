package marketplace.admin.api.marketplace_Admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import marketplace.admin.api.marketplace_Admin.enititys.Emails;

public interface EmailRepository extends JpaRepository<Emails, Integer> {
	public Emails findByEmailId(int emailId);
}
