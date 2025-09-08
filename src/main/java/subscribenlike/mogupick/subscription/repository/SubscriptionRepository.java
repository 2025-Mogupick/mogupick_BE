package subscribenlike.mogupick.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.subscription.domain.Subscription;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByNextPaymentDate(LocalDate nextPaymentDate);
}