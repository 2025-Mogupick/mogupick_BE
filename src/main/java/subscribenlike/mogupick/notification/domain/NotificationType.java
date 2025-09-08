package subscribenlike.mogupick.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    DELIVERY_STARTED("배송 시작 알림", "'%s' 상품의 배송이 시작되었습니다."),
    PAYMENT_REMINDER("결제 예정 알림", "'%s' 상품의 결제 예정일이 3일 남았습니다."),
    PAYMENT_COMPLETED("결제 완료 알림", "'%s' 상품 결제가 정상적으로 완료되었습니다."),
    NEW_REVIEW("새로운 리뷰 알림", "'%s' 상품에 새로운 리뷰가 달렸습니다.");

    private final String description;
    private final String contentTemplate;

    public String createContent(String subject) {
        return String.format(this.contentTemplate, subject);
    }
}