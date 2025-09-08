package subscribenlike.mogupick.billing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import subscribenlike.mogupick.billing.domain.PaymentStatus;
import subscribenlike.mogupick.billing.service.BillingKeyService;
import subscribenlike.mogupick.billing.service.PaymentService;
import subscribenlike.mogupick.billing.service.client.TossPaymentsClient;
import subscribenlike.mogupick.billing.domain.PaymentState;
import subscribenlike.mogupick.billing.dto.PaymentStateResponse;
import subscribenlike.mogupick.billing.repository.PaymentStateRepository;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @Mock
    private TossPaymentsClient tossPaymentsClient;

    @Mock
    private BillingKeyService billingKeyService;

    @Mock
    private PaymentStateRepository paymentStateRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void charge_정상케이스() {
        String orderId = "id";
        String customerKey = "customer";
        int amount = 1000;
        String orderName = "test order";
        String billingKey = "billingKey";
        Map<String, Object> mockResponse = Map.of("paymentKey", "PK123");

        when(paymentStateRepository.save(org.mockito.ArgumentMatchers.any(PaymentState.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(billingKeyService.loadDecrypted(customerKey)).thenReturn(billingKey);
        when(tossPaymentsClient.approveBilling(billingKey, amount, customerKey, orderId, orderName)).thenReturn(mockResponse);

        PaymentStateResponse result = paymentService.charge(orderId, customerKey, orderName, amount);

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(PaymentStatus.APPROVED);

        verify(paymentStateRepository).save(org.mockito.ArgumentMatchers.any(PaymentState.class));
        verify(billingKeyService).loadDecrypted(customerKey);
        verify(tossPaymentsClient).approveBilling(billingKey, amount, customerKey, orderId, orderName);
    }
}
