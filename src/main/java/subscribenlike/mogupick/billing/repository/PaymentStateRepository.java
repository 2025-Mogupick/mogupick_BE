package subscribenlike.mogupick.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.billing.domain.PaymentState;

import java.util.Optional;

public interface PaymentStateRepository extends JpaRepository<PaymentState, Long> {
    Optional<PaymentState> findByOrderId(String orderId);
}
