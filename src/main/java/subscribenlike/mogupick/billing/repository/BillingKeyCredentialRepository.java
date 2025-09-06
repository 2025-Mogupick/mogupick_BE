package subscribenlike.mogupick.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.billing.domain.BillingKeyCredential;

import java.util.Optional;

public interface BillingKeyCredentialRepository extends JpaRepository<BillingKeyCredential, Long> {
    Optional<BillingKeyCredential> findByCustomerKey(String customerKey);
}
