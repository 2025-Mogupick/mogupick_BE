package subscribenlike.mogupick.subscriptionOption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;

import java.util.List;

public interface SubscriptionOptionRepository extends JpaRepository<SubscriptionOption, Long> {
    List<SubscriptionOption> findAllByOrderByUnitAscPeriodAsc();
}
