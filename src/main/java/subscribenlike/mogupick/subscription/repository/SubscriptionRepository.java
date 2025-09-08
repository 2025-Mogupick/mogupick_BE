package subscribenlike.mogupick.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.subscription.domain.Subscription;
import subscribenlike.mogupick.subscription.domain.SubscriptionStatus;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByMemberId(Long memberId);
    List<Subscription> findByMemberIdAndStatus(Long memberId, SubscriptionStatus status);
    Optional<Subscription> findByPaymentKey(String paymentKey);
    boolean existsByPaymentKey(String paymentKey);
}

