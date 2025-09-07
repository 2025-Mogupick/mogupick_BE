package subscribenlike.mogupick.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.notification.domain.Notification;
import subscribenlike.mogupick.notification.domain.NotificationType;
import subscribenlike.mogupick.notification.repository.NotificationRepository;

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
}