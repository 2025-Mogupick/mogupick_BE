package subscribenlike.mogupick.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.notification.common.exception.NotificationErrorCode;
import subscribenlike.mogupick.notification.common.exception.NotificationException;
import subscribenlike.mogupick.notification.domain.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    default Notification getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));
    }
}