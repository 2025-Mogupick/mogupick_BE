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
    public void registerPaymentMethod(String authKey, String customerKey) {
        log.info("billingKey.register start customerKey={}", mask(customerKey));
        Map<String, Object> res = toss.issueBillingKeyByAuthKey(authKey, customerKey);
        String billingKey = (String) res.get("billingKey");
        if (billingKey == null) {
            log.error("billingKey.register fail no billingKey customerKey={}", mask(customerKey));
            throw new BillingException(BillingErrorCode.BILLING_KEY_ISSUE_FAILED);
        }
        String enc = aes.encrypt(billingKey);
        String alias = "billingKey";
        billingKeyCredentialRepository.findByCustomerKey(customerKey)
                .map(b -> {
                    b.rotate(enc, alias);
                    return b;
                })
                .orElseGet(() -> billingKeyCredentialRepository.save(BillingKeyCredential.of(customerKey, enc, alias)));
        log.info("billingKey.register success customerKey={}", mask(customerKey));
    }

    @Transactional
    public void updatePaymentMethod(String authKey, String customerKey) {
        log.info("billingKey.update start customerKey={}", mask(customerKey));
        Map<String, Object> res = toss.issueBillingKeyByAuthKey(authKey, customerKey);
        String billingKey = (String) res.get("billingKey");
        if (billingKey == null) {
            log.error("billingKey.update fail no billingKey customerKey={}", mask(customerKey));
            throw new BillingException(BillingErrorCode.BILLING_KEY_ISSUE_FAILED);
        }
        String enc = aes.encrypt(billingKey);
        String alias = "billingKey";
        billingKeyCredentialRepository.findByCustomerKey(customerKey)
                .map(b -> {
                    b.rotate(enc, alias);
                    return b;
                })
                .orElseThrow(() -> new BillingException(BillingErrorCode.NO_BILLING_KEY));
        log.info("billingKey.update success customerKey={}", mask(customerKey));
    }

    @Transactional
    public void deletePaymentMethod(String customerKey) {
        log.info("billingKey.delete start customerKey={}", mask(customerKey));
        billingKeyCredentialRepository.findByCustomerKey(customerKey)
                .ifPresentOrElse(
                        cred -> {
                            billingKeyCredentialRepository.delete(cred);
                            log.info("billingKey.delete success customerKey={}", mask(customerKey));
                        },
                        () -> {
                            log.warn("billingKey.delete not found customerKey={}", mask(customerKey));
                            throw new BillingException(BillingErrorCode.NO_BILLING_KEY);
                        }
                );
    }

    @Transactional(readOnly = true)
    public String loadDecrypted(String customerKey) {
        var cred = billingKeyCredentialRepository.findByCustomerKey(customerKey)
                .orElseThrow(() -> new BillingException(BillingErrorCode.NO_BILLING_KEY));
        return aes.decrypt(cred.getBillingKeyEnc());
    }

    private String mask(String str) {
        if (str == null || str.length() < 4) return "***";
        return str.substring(0, 2) + "***";
    }
}
