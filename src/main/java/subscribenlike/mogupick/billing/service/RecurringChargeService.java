package subscribenlike.mogupick.billing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.billing.domain.PaymentState;
import subscribenlike.mogupick.billing.dto.PaymentStateResponse;
import subscribenlike.mogupick.billing.repository.PaymentStateRepository;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringChargeService {
    private final PaymentStateRepository paymentStateRepository;
    private final PaymentService paymentService;

    @Transactional
    public PaymentStateResponse charge(String orderId, String customerKey, String orderName, int amount) {
        PaymentState st;
        try {
            st = paymentStateRepository.save(PaymentState.requested(orderId, customerKey, amount));
            log.info("charge.requested orderId={} amount={}", orderId, amount);
        } catch (DataIntegrityViolationException dup) {
            var existing = paymentStateRepository.findByOrderId(orderId).orElseThrow();
            log.info("charge.idempotent-hit orderId={} status={}", orderId, existing.getStatus());
            return PaymentStateResponse.from(existing);
        }

        try {
            Map<String, Object> res = paymentService.approve(orderId, customerKey, amount, orderName);
            Object paymentKey = res.get("paymentKey");
            if (paymentKey == null) throw new IllegalStateException("approve returned no paymentKey");
            st.markApproved(paymentKey.toString());
            log.info("charge.approved orderId={} paymentKey={}", orderId, paymentKey);
            return PaymentStateResponse.from(st);
        } catch (Exception e) {
            st.markFailed(e.getMessage());
            log.error("charge.failed orderId={} msg={}", orderId, e.getMessage());
            throw e;
        }
    }
}
