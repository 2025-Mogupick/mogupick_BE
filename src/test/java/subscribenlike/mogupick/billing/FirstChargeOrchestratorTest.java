package subscribenlike.mogupick.billing;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import subscribenlike.mogupick.billing.domain.PaymentState;
import subscribenlike.mogupick.billing.dto.PaymentStateResponse;
import subscribenlike.mogupick.billing.repository.PaymentStateRepository;
import subscribenlike.mogupick.billing.service.BillingKeyService;
import subscribenlike.mogupick.billing.service.FirstChargeOrchestrator;
import subscribenlike.mogupick.billing.service.PaymentService;
import subscribenlike.mogupick.support.fixture.BillingFixture;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FirstChargeOrchestratorTest {

    @Mock
    private PaymentStateRepository paymentStateRepository;

    @Mock
    private BillingKeyService billingKeyService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private FirstChargeOrchestrator orchestrator;

    public FirstChargeOrchestratorTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void firstCharge_정상_케이스() {
        String orderId = "orderId";
        String authKey = "authKey";
        String customerKey = "customerKey";
        String orderName = "orderName";
        int amount = 1000;

        PaymentState st = BillingFixture.요청상태(orderId, customerKey, amount);
        Map<String, Object> paymentResult = Map.of("paymentKey", "abc123");

        when(paymentStateRepository.save(any(PaymentState.class))).thenReturn(st);
        when(paymentService.approve(orderId, customerKey, amount, orderName)).thenReturn(paymentResult);

        PaymentStateResponse res = orchestrator.firstCharge(orderId, authKey, customerKey, orderName, amount);

        assertThat(res.orderId()).isEqualTo(orderId);
        verify(billingKeyService).issueAndStore(orderId, authKey, customerKey);
        verify(paymentService).approve(orderId, customerKey, amount, orderName);
    }

    @Test
    void firstCharge_중복_orderId_멱등성() {
        String orderId = "orderId";
        String customerKey = "customerKey";
        int amount = 1000;
        PaymentState existing = BillingFixture.요청상태(orderId, customerKey, amount);

        when(paymentStateRepository.save(any(PaymentState.class))).thenThrow(new DataIntegrityViolationException("dup"));
        when(paymentStateRepository.findByOrderId(orderId)).thenReturn(Optional.of(existing));

        PaymentStateResponse res = orchestrator.firstCharge(orderId, "authKey", customerKey, "orderName", amount);

        assertThat(res.orderId()).isEqualTo(orderId);
        verify(paymentStateRepository).save(any(PaymentState.class));
    }
}
