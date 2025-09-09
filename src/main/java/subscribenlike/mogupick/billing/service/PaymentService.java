package subscribenlike.mogupick.billing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.billing.common.exception.BillingErrorCode;
import subscribenlike.mogupick.billing.common.exception.BillingException;
import subscribenlike.mogupick.billing.domain.PaymentState;
import subscribenlike.mogupick.billing.dto.PaymentStateResponse;
import subscribenlike.mogupick.billing.repository.PaymentStateRepository;
import subscribenlike.mogupick.billing.service.client.TossPaymentsClient;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.notification.domain.NotificationType;
import subscribenlike.mogupick.notification.service.NotificationService;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final TossPaymentsClient toss;
    private final BillingKeyService billingKeyService;
    private final PaymentStateRepository paymentStateRepository;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;

    @Transactional
    public PaymentStateResponse charge(String orderId, String customerKey, String orderName, int amount) {
        PaymentState st;
        try {
            st = paymentStateRepository.save(PaymentState.requested(orderId, customerKey, amount));
            log.info("charge.requested orderId={} amount={} customerKey={}", orderId, amount, mask(customerKey));
        } catch (DataIntegrityViolationException dup) {
            var existing = paymentStateRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new BillingException(BillingErrorCode.PAYMENT_STATE_NOT_FOUND));
            log.info("charge.idempotent-hit orderId={} status={}", orderId, existing.getStatus());
            return PaymentStateResponse.from(existing);
        }

        try {
            String billingKey = billingKeyService.loadDecrypted(customerKey);
            if (billingKey == null) {
                throw new BillingException(BillingErrorCode.NO_BILLING_KEY);
            }
            Map<String, Object> res = toss.approveBilling(billingKey, amount, customerKey, orderId, orderName);
            Object paymentKey = res.get("paymentKey");
            if (paymentKey == null) {
                throw new BillingException(BillingErrorCode.PAYMENT_APPROVAL_FAILED);
            }
            st.markApproved(paymentKey.toString());
            log.info("charge.approved orderId={} paymentKey={}", orderId, paymentKey);

            Member member = memberRepository.findByEmailOrThrow(customerKey);
            String content = NotificationType.PAYMENT_COMPLETED.createContent(orderName);
            notificationService.createNotification(
                    member,
                    NotificationType.PAYMENT_COMPLETED,
                    content,
                    "/alert"
            );

            return PaymentStateResponse.from(st);
        } catch (BillingException e) {
            st.markFailed(e.getMessage());
            log.error("charge.failed orderId={} billingException msg={}", orderId, e.getMessage());
            throw e;
        } catch (Exception e) {
            st.markFailed(e.getMessage());
            log.error("charge.failed orderId={} unexpectedException msg={}", orderId, e.getMessage());
            throw new BillingException(BillingErrorCode.PAYMENT_APPROVAL_FAILED, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Optional<PaymentState> findPaymentStateByOrderId(String orderId) {
        return paymentStateRepository.findByOrderId(orderId);
    }

    private String mask(String str) {
        if (str == null || str.length() < 4) return "***";
        return str.substring(0, 2) + "***";
    }
}