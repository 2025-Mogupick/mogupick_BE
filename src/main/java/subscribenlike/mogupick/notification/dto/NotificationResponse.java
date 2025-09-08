package subscribenlike.mogupick.notification.dto;

import lombok.Getter;
import subscribenlike.mogupick.notification.domain.Notification;
import subscribenlike.mogupick.notification.domain.NotificationType;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {

    private final Long id;
    private final String content;
    private final String url;
    private final boolean isRead;
    private final NotificationType notificationType;
    private final LocalDateTime createdAt;

    // Notification 엔티티를 NotificationResponse DTO로 변환하는 생성자
    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.url = notification.getUrl();
        this.isRead = notification.getIsRead();
        this.notificationType = notification.getNotificationType();
        this.createdAt = notification.getCreatedAt();
    }
}