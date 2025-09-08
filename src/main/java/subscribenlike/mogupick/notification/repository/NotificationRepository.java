package subscribenlike.mogupick.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}