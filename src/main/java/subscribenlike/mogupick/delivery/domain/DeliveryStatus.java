package subscribenlike.mogupick.delivery.domain;

public enum DeliveryStatus {
    PENDING, // 배송 준비중
    SHIPPED, // 배송 시작
    DELIVERED, // 배송 완료
    CANCELLED // 주문 취소
}