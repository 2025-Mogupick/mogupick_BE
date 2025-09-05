package subscribenlike.mogupick.billing;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import subscribenlike.mogupick.billing.common.exception.BillingException;
import subscribenlike.mogupick.billing.domain.BillingKeyCredential;
import subscribenlike.mogupick.billing.repository.BillingKeyCredentialRepository;
import subscribenlike.mogupick.billing.service.BillingKeyService;
import subscribenlike.mogupick.billing.service.client.TossPaymentsClient;
import subscribenlike.mogupick.billing.util.AesGcm;
import subscribenlike.mogupick.support.fixture.BillingFixture;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BillingKeyServiceTest {

    @Mock
    private TossPaymentsClient tossPaymentsClient;

    @Mock
    private BillingKeyCredentialRepository billingKeyCredentialRepository;

    @Mock
    private AesGcm aesGcm;

    @InjectMocks
    private BillingKeyService billingKeyService;

    public BillingKeyServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void issueAndStore_빌링키_정상발급_저장() {
        String orderId = "order1";
        String authKey = "authKey";
        String customerKey = "customer1";
        String billingKey = "billingKey";
        String enc = "encBillingKey";
        Map<String, Object> tossRes = Map.of("billingKey", billingKey);

        when(tossPaymentsClient.issueBillingKeyByAuthKey(authKey, customerKey)).thenReturn(tossRes);
        when(aesGcm.encrypt(billingKey)).thenReturn(enc);
        when(billingKeyCredentialRepository.findByCustomerKey(customerKey)).thenReturn(Optional.empty());
        when(billingKeyCredentialRepository.save(any(BillingKeyCredential.class))).thenAnswer(i -> i.getArguments()[0]);

        billingKeyService.issueAndStore(orderId, authKey, customerKey);

        verify(tossPaymentsClient).issueBillingKeyByAuthKey(authKey, customerKey);
        verify(billingKeyCredentialRepository).save(any(BillingKeyCredential.class));
    }

    @Test
    void loadDecrypted_존재하는_빌링키_복호화() {
        String customerKey = "customer1";
        String enc = "encBillingKey";
        String billingKey = "billingKey";

        BillingKeyCredential credential = BillingFixture.빌링키자격(customerKey, enc, "alias");

        when(billingKeyCredentialRepository.findByCustomerKey(customerKey)).thenReturn(Optional.of(credential));
        when(aesGcm.decrypt(enc)).thenReturn(billingKey);

        String result = billingKeyService.loadDecrypted(customerKey);

        assertThat(result).isEqualTo(billingKey);
        verify(aesGcm).decrypt(enc);
    }

    @Test
    void loadDecrypted_빌링키_없으면_예외() {
        String customerKey = "nonExistent";
        when(billingKeyCredentialRepository.findByCustomerKey(customerKey)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billingKeyService.loadDecrypted(customerKey))
                .isInstanceOf(BillingException.class)
                .hasMessageContaining("등록된 빌링키가 없습니다");
    }
}
