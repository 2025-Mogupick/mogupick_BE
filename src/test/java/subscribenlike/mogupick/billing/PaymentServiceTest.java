package subscribenlike.mogupick.billing;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import subscribenlike.mogupick.billing.service.BillingKeyService;
import subscribenlike.mogupick.billing.service.PaymentService;
import subscribenlike.mogupick.billing.service.client.TossPaymentsClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @Mock
    private TossPaymentsClient tossPaymentsClient;

    @Mock
    private BillingKeyService billingKeyService;

    @InjectMocks
    private PaymentService paymentService;

    public PaymentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void approve_정상케이스() {
        String orderId = "id";
        String customerKey = "customer";
        int amount = 1000;
        String orderName = "test order";
        String billingKey = "billingKey";
        Map<String, Object> mockResponse = Map.of("paymentKey", "PK123");

        when(billingKeyService.loadDecrypted(customerKey)).thenReturn(billingKey);
        when(tossPaymentsClient.approveBilling(billingKey, amount, customerKey, orderId, orderName)).thenReturn(mockResponse);

        Map<String, Object> result = paymentService.approve(orderId, customerKey, amount, orderName);

        assertThat(result).isEqualTo(mockResponse);
        verify(billingKeyService).loadDecrypted(customerKey);
        verify(tossPaymentsClient).approveBilling(billingKey, amount, customerKey, orderId, orderName);
    }
}
