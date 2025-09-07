package subscribenlike.mogupick.notification.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import subscribenlike.mogupick.notification.domain.NotificationType;
import subscribenlike.mogupick.notification.service.NotificationService;
import subscribenlike.mogupick.subscription.domain.Subscription;
import subscribenlike.mogupick.subscription.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationService notificationService;
    private final SubscriptionRepository subscriptionRepository;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendPaymentReminderNotifications() {
        log.info("결제 3일 전 알림 스케줄러를 시작합니다.");

        LocalDate targetDate = LocalDate.now().plusDays(3);

        List<Subscription> subscriptions = subscriptionRepository.findByNextPaymentDate(targetDate);

        for (Subscription subscription : subscriptions) {
            String content = String.format(
                    "'%s' 상품의 결제 예정일이 3일 남았습니다.",
                    subscription.getProduct().getName()
            );

            notificationService.createNotification(
                    subscription.getMember(),
                    NotificationType.PAYMENT_REMINDER,
                    content,
                    "/my-page/subscriptions"
            );
        }
        log.info("총 {}개의 결제 3일 전 알림 생성을 완료했습니다.", subscriptions.size());
    }
}