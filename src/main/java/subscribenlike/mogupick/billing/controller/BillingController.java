package subscribenlike.mogupick.billing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.billing.dto.*;
import subscribenlike.mogupick.billing.service.BillingKeyService;
import subscribenlike.mogupick.billing.service.PaymentService;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.member.repository.MemberRepository;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/billing")
public class BillingController {
    private final BillingKeyService billingKeyService;
    private final PaymentService paymentService;
    private final MemberRepository memberRepository;

    @Operation(summary = "결제수단 등록", description = "고객의 결제수단을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "결제수단 등록 성공")
    })
    @PostMapping("/payment-methods")
    public ResponseEntity<Void> registerPaymentMethod(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RegisterPaymentMethodRequest req
    ) {
        String customerKey = memberRepository.findOrThrow(userDetails.getMemberId()).getCustomerKey();
        log.info("api.registerPaymentMethod customerKey={}", mask(customerKey));
        billingKeyService.registerPaymentMethod(req.authKey(), customerKey);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "결제수단 변경", description = "고객의 결제수단을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제수단 변경 성공")
    })
    @PutMapping("/payment-methods")
    public ResponseEntity<Void> updatePaymentMethod(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdatePaymentMethodRequest req
    ) {
        String customerKey = memberRepository.findOrThrow(userDetails.getMemberId()).getCustomerKey();
        log.info("api.updatePaymentMethod customerKey={}", mask(customerKey));
        billingKeyService.updatePaymentMethod(req.authKey(), customerKey);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "결제수단 삭제", description = "고객의 결제수단을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "결제수단 삭제 성공")
    })
    @DeleteMapping("/payment-methods/{customerKey}")
    public ResponseEntity<Void> deletePaymentMethod(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String customerKey = memberRepository.findOrThrow(userDetails.getMemberId()).getCustomerKey();
        log.info("api.deletePaymentMethod customerKey={}", mask(customerKey));
        billingKeyService.deletePaymentMethod(customerKey);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "결제 요청", description = "주문에 대해 결제를 요청합니다. 최초 결제 및 재결제를 통합 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 요청 성공")
    })
    @PostMapping("/charge")
    public ResponseEntity<PaymentStateResponse> charge(@RequestBody ChargeRequest req) {
        log.info("api.charge orderId={} amount={} customerKey={}", req.orderId(), req.amount(), mask(req.customerKey()));
        return ResponseEntity.ok(paymentService.charge(req.orderId(), req.customerKey(), req.orderName(), req.amount()));
    }

    @Operation(summary = "결제 상태 조회", description = "주문 ID로 결제 상태를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 상태 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 주문 ID를 찾을 수 없음")
    })
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
