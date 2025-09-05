package subscribenlike.mogupick.billing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.billing.service.client.TossPaymentsClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final TossPaymentsClient toss;
    private final BillingKeyService billingKeyService;

    @Transactional(noRollbackFor = IllegalStateException.class)
    public Map<String, Object> approve(String orderId, String customerKey, int amount, String orderName) {
        String billingKey = billingKeyService.loadDecrypted(customerKey);
        return toss.approveBilling(billingKey, amount, customerKey, orderId, orderName);
    }
}
