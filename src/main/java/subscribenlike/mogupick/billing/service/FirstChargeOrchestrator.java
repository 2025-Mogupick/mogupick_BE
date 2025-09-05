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

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirstChargeOrchestrator {
    private final PaymentStateRepository paymentStateRepository;
    private final BillingKeyService billingKeyService;
    private final PaymentService paymentService;

    @Transactional
    public PaymentStateResponse firstCharge(String orderId, String authKey, String customerKey, String orderName, int amount) {
        // 0) 멱등 INSERT 선점
        PaymentState st;
        try {
            st = paymentStateRepository.save(PaymentState.requested(orderId, customerKey, amount));
            log.info("firstCharge.requested orderId={} amount={} customerKey={}", orderId, amount, mask(customerKey));
        } catch (DataIntegrityViolationException dup) {
            var existing = paymentStateRepository.findByOrderId(orderId).orElseThrow(
                    () -> new BillingException(BillingErrorCode.PAYMENT_STATE_NOT_FOUND, orderId)
            );
            log.info("firstCharge.idempotent-hit orderId={} status={}", orderId, existing.getStatus());
            return PaymentStateResponse.from(existing);
        }

        try {
            billingKeyService.issueAndStore(orderId, authKey, customerKey);
            st.markBillingKeyIssued();

            Map<String, Object> res = paymentService.approve(orderId, customerKey, amount, orderName);
            Object paymentKey = res.get("paymentKey");
            if (paymentKey == null) {
                throw new BillingException(BillingErrorCode.PAYMENT_APPROVAL_FAILED);
            }
            st.markApproved(paymentKey.toString());

            log.info("firstCharge.approved orderId={} paymentKey={}", orderId, paymentKey);
            return PaymentStateResponse.from(st);
        } catch (Exception e) {
            st.markFailed(e.getMessage());
            log.error("firstCharge.failed orderId={} msg={}", orderId, e.getMessage());
            throw new BillingException(BillingErrorCode.PAYMENT_APPROVAL_FAILED, e.getMessage());
        }
    }

    private String mask(String str) {
        if (str == null || str.length() < 4) {
            return "***";
        }
        return str.substring(0, 2) + "***";
    }
}
