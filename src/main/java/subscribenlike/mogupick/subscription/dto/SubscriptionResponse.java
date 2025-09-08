package subscribenlike.mogupick.subscription.dto;

import subscribenlike.mogupick.subscription.domain.Subscription;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SubscriptionResponse(
        Long subscriptionId,
        String productName,
        String productDescription,
        String origin,
        int price,
        String brandName,
        String deliveryCycle,
        LocalDate firstDeliveryDate,
        LocalDate nextBillingDate,
        int progressRound,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SubscriptionResponse from(Subscription sub) {
        var product = sub.getProduct();
        var option = sub.getOption();
        return new SubscriptionResponse(
                sub.getId(),
                product.getName(),
                product.getDescription(),
                product.getOrigin(),
                product.getPrice(),
                product.getBrandName(),
                option.getDisplayText(),
                sub.getFirstDeliveryDate(),
                sub.getNextBillingDate(),
                sub.getProgressRound(),
                sub.getStatus().name(),
                sub.getCreatedAt(),
                sub.getUpdatedAt()
        );
    }
}
