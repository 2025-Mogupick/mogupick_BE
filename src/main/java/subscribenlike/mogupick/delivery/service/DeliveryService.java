package subscribenlike.mogupick.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.delivery.domain.Delivery;
import subscribenlike.mogupick.delivery.repository.DeliveryRepository;
import subscribenlike.mogupick.notification.domain.NotificationType;
import subscribenlike.mogupick.notification.service.NotificationService;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final NotificationService notificationService;

    public void startDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.getById(deliveryId);

        delivery.start();

        String content = NotificationType.DELIVERY_STARTED.createContent(delivery.getProduct().getName());

        notificationService.createNotification(
                delivery.getMember(),
                NotificationType.DELIVERY_STARTED,
                content,
                "/alert"
        );
    }
}