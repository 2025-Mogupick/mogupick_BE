package subscribenlike.mogupick.support.fixture;

import subscribenlike.mogupick.billing.domain.BillingKeyCredential;
import subscribenlike.mogupick.billing.domain.PaymentState;

public class BillingFixture {

    public static BillingKeyCredential 빌링키자격(String customerKey, String billingKeyEnc, String keyAlias) {
        return BillingKeyCredential.of(customerKey, billingKeyEnc, keyAlias);
    }

    public static PaymentState 요청상태(String orderId, String customerKey, int amount) {
        return PaymentState.requested(orderId, customerKey, amount);
    }

    public static PaymentState 승인상태(String orderId, String customerKey, int amount, String paymentKey) {
        PaymentState state = PaymentState.requested(orderId, customerKey, amount);
        state.markApproved(paymentKey);
        return state;
    }

    public static PaymentState 실패상태(String orderId, String customerKey, int amount, String errorMsg) {
        PaymentState state = PaymentState.requested(orderId, customerKey, amount);
        state.markFailed(errorMsg);
        return state;
    }
}

