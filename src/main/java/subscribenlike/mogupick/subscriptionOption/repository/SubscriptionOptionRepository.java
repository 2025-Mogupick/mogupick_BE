package subscribenlike.mogupick.subscriptionOption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;

public interface SubscriptionOptionRepository extends JpaRepository<SubscriptionOption, Long> {
}
