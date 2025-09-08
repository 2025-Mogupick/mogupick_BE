package subscribenlike.mogupick.billing.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.billing.dto.*;
import subscribenlike.mogupick.billing.service.BillingKeyService;
import subscribenlike.mogupick.billing.service.PaymentService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/billing")
public class BillingController {
    private final BillingKeyService billingKeyService;
    private final PaymentService paymentService;

    // 결제수단 등록 API
    @PostMapping("/payment-methods")
    public ResponseEntity<Void> registerPaymentMethod(@RequestBody RegisterPaymentMethodRequest req) {
        log.info("api.registerPaymentMethod customerKey={}", mask(req.customerKey()));
        billingKeyService.registerPaymentMethod(req.authKey(), req.customerKey());
        return ResponseEntity.ok().build();
    }

    // 결제수단 변경 API
    @PutMapping("/payment-methods")
    public ResponseEntity<Void> updatePaymentMethod(@RequestBody UpdatePaymentMethodRequest req) {
        log.info("api.updatePaymentMethod customerKey={}", mask(req.customerKey()));
        billingKeyService.updatePaymentMethod(req.authKey(), req.customerKey());
        return ResponseEntity.ok().build();
    }

    // 결제수단 삭제 API
    @DeleteMapping("/payment-methods/{customerKey}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable String customerKey) {
        log.info("api.deletePaymentMethod customerKey={}", mask(customerKey));
        billingKeyService.deletePaymentMethod(customerKey);
        return ResponseEntity.noContent().build();
    }

    // 결제 API (최초 결제 및 재결제 통합)
    @PostMapping("/charge")
    public ResponseEntity<PaymentStateResponse> charge(@RequestBody ChargeRequest req) {
        log.info("api.charge orderId={} amount={} customerKey={}", req.orderId(), req.amount(), mask(req.customerKey()));
        return ResponseEntity.ok(paymentService.charge(req.orderId(), req.customerKey(), req.orderName(), req.amount()));
    }

    // 결제 상태 조회 API
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<PaymentStateResponse> getState(@PathVariable String orderId) {
        return ResponseEntity.of(
                paymentService.findPaymentStateByOrderId(orderId).map(PaymentStateResponse::from)
        );
    }

    private String mask(String v) {
        return (v == null || v.length() < 4) ? "***" : v.substring(0, 2) + "***";
    }
}
