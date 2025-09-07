package subscribenlike.mogupick.subscription.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;

import java.util.List;

public interface SubscriptionOptionRepository extends JpaRepository<SubscriptionOption, Long> {
    List<Subscription> findByProductId(Long productId);
    List<Subscription> findByMemberId(Long memberId);
}
