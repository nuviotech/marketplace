package marketplace.admin.api.marketplace_Admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import marketplace.admin.api.marketplace_Admin.enititys.Policys;

public interface PolicyRepository extends JpaRepository<Policys, Integer> {

	Policys findByPolicyType(String type);

}
