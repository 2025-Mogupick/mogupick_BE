package subscribenlike.mogupick.billing;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import subscribenlike.mogupick.billing.domain.PaymentState;
import subscribenlike.mogupick.billing.dto.PaymentStateResponse;
import subscribenlike.mogupick.billing.repository.PaymentStateRepository;
import subscribenlike.mogupick.billing.service.PaymentService;
import subscribenlike.mogupick.billing.service.RecurringChargeService;
import subscribenlike.mogupick.support.fixture.BillingFixture;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecurringChargeServiceTest {

    @Mock
    private PaymentStateRepository paymentStateRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private RecurringChargeService recurringChargeService;

    public RecurringChargeServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void charge_정상케이스() {
        String orderId = "orderId";
        String customerKey = "customerKey";
        String orderName = "order";
        int amount = 5000;

        PaymentState st = BillingFixture.요청상태(orderId, customerKey, amount);
        Map<String, Object> approveRes = Map.of("paymentKey", "abc123");

        when(paymentStateRepository.save(any(PaymentState.class))).thenReturn(st);
        when(paymentService.approve(orderId, customerKey, amount, orderName)).thenReturn(approveRes);

        PaymentStateResponse res = recurringChargeService.charge(orderId, customerKey, orderName, amount);

        assertThat(res.orderId()).isEqualTo(orderId);
        verify(paymentService).approve(orderId, customerKey, amount, orderName);
    }

    @Test
    void charge_멱등성_중복처리() {
        String orderId = "orderId";
        String customerKey = "customerKey";
        String orderName = "order";
        int amount = 1234;

        PaymentState existing = BillingFixture.요청상태(orderId, customerKey, amount);

        when(paymentStateRepository.save(any(PaymentState.class))).thenThrow(new DataIntegrityViolationException("dup"));
        when(paymentStateRepository.findByOrderId(orderId)).thenReturn(Optional.of(existing));

        PaymentStateResponse res = recurringChargeService.charge(orderId, customerKey, orderName, amount);

        assertThat(res.orderId()).isEqualTo(orderId);
    }
}
