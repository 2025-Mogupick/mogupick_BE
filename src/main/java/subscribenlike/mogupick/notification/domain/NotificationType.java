package subscribenlike.mogupick.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    DELIVERY_STARTED("배송 시작 알림"),
    PAYMENT_REMINDER("결제 예정 알림"),
    PAYMENT_COMPLETED("결제 완료 알림"),
    NEW_REVIEW("새로운 리뷰 알림");

    private final String description;
}