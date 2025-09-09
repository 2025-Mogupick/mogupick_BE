package subscribenlike.mogupick.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.delivery.common.exception.DeliveryErrorCode;
import subscribenlike.mogupick.delivery.common.exception.DeliveryException;
import subscribenlike.mogupick.delivery.domain.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    default Delivery getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
    }
}