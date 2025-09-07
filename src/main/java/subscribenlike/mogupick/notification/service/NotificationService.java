package subscribenlike.mogupick.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.notification.common.exception.NotificationErrorCode;
import subscribenlike.mogupick.notification.common.exception.NotificationException;
import subscribenlike.mogupick.notification.domain.Notification;
import subscribenlike.mogupick.notification.domain.NotificationType;
import subscribenlike.mogupick.notification.dto.NotificationResponse;
import subscribenlike.mogupick.notification.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(Member member, NotificationType type, String content, String url) {
        Notification notification = Notification.builder()
                .member(member)
                .notificationType(type)
                .content(content)
                .url(url)
                .build();
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
        return notifications.stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toList());
    }

    public void readNotification(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getMember().getId().equals(memberId)) {
            throw new NotificationException(NotificationErrorCode.FORBIDDEN_READ_NOTIFICATION);
        }

        notification.read();
    }
}