package subscribenlike.mogupick.billing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.billing.common.exception.BillingErrorCode;
import subscribenlike.mogupick.billing.common.exception.BillingException;
import subscribenlike.mogupick.billing.domain.BillingKeyCredential;
import subscribenlike.mogupick.billing.repository.BillingKeyCredentialRepository;
import subscribenlike.mogupick.billing.service.client.TossPaymentsClient;
import subscribenlike.mogupick.billing.util.AesGcm;

import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class BillingKeyService {
    private final TossPaymentsClient toss;
    private final BillingKeyCredentialRepository billingKeyCredentialRepository;
    private final AesGcm aes;

    @Transactional
    public void issueAndStore(String orderId, String authKey, String customerKey) {
        log.info("billingKey.issue start orderId={} customerKey={}", orderId, mask(customerKey));
        Map<String, Object> res = toss.issueBillingKeyByAuthKey(authKey, customerKey);

        String billingKey = (String) res.get("billingKey");
        if (billingKey == null) {
            log.error("billingKey.issue fail orderId={} reason=no-billingKey", orderId);
            throw new BillingException(BillingErrorCode.BILLING_KEY_ISSUE_FAILED);
        }

        String enc = aes.encrypt(billingKey);
        String alias = "billingKey"; // 필요시 카드 끝4자리 등으로 마스킹 이름 구성

        billingKeyCredentialRepository.findByCustomerKey(customerKey)
                .map(b -> {
                    b.rotate(enc, alias);
                    return b;
                })
                .orElseGet(() -> billingKeyCredentialRepository.save(BillingKeyCredential.of(customerKey, enc, alias)));

        log.info("billingKey.issue success orderId={} customerKey={}", orderId, mask(customerKey));
    }

    @Transactional(readOnly = true)
    public String loadDecrypted(String customerKey) {
        var cred = billingKeyCredentialRepository.findByCustomerKey(customerKey)
                .orElseThrow(() -> new BillingException(BillingErrorCode.NO_BILLING_KEY, customerKey));
        return aes.decrypt(cred.getBillingKeyEnc());
    }

    private String mask(String str) {
        if (str == null || str.length() < 4) {
            return "***";
        }
        return str.substring(0, 2) + "***";
    }
}
