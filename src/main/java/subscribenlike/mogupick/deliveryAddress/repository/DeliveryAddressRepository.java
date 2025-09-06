package subscribenlike.mogupick.deliveryAddress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.deliveryAddress.domain.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    List<DeliveryAddress> findByMemberId(Long memberId);
}
