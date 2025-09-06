package subscribenlike.mogupick.billing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.billing.dto.ChargeRequest;
import subscribenlike.mogupick.billing.dto.FirstChargeRequest;
import subscribenlike.mogupick.billing.dto.PaymentStateResponse;
import subscribenlike.mogupick.billing.repository.PaymentStateRepository;
import subscribenlike.mogupick.billing.service.FirstChargeOrchestrator;
import subscribenlike.mogupick.billing.service.RecurringChargeService;
import subscribenlike.mogupick.billing.common.success.BillingSuccessCode;
import subscribenlike.mogupick.common.success.SuccessResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final FirstChargeOrchestrator firstChargeOrchestrator;
    private final RecurringChargeService recurringChargeService;
    private final PaymentStateRepository stateRepo;

    @Operation(summary = "최초 결제", description = "authKey 기반 빌링키 발급 및 결제 승인 처리(멱등)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최초 결제 성공"),
    })
    @PostMapping("/first-charge")
    public ResponseEntity<?> firstCharge(@Valid @RequestBody FirstChargeRequest req) {
        log.info("api.firstCharge orderId={} amount={} customerKey={}",
                req.orderId(), req.amount(), mask(req.customerKey()));
        PaymentStateResponse response = firstChargeOrchestrator.firstCharge(
                req.orderId(), req.authKey(), req.customerKey(), req.orderName(), req.amount()
        );
        return ResponseEntity
                .status(BillingSuccessCode.FIRST_CHARGE_SUCCESS.getStatus())
                .body(SuccessResponse.from(BillingSuccessCode.FIRST_CHARGE_SUCCESS, response));
    }

    @Operation(summary = "재결제", description = "저장된 빌링키로 재결제 승인 처리(멱등)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재결제 성공"),
    })
    @PostMapping("/charge")
    public ResponseEntity<?> charge(@Valid @RequestBody ChargeRequest req) {
        log.info("api.charge orderId={} amount={} customerKey={}",
                req.orderId(), req.amount(), mask(req.customerKey()));
        PaymentStateResponse response = recurringChargeService.charge(
                req.orderId(), req.customerKey(), req.orderName(), req.amount()
        );
        return ResponseEntity
                .status(BillingSuccessCode.RECURRING_CHARGE_SUCCESS.getStatus())
                .body(SuccessResponse.from(BillingSuccessCode.RECURRING_CHARGE_SUCCESS, response));
    }

    @Operation(summary = "결제 상태 조회", description = "orderId에 해당하는 결제 상태 최신 스냅샷 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 상태 조회 성공"),
    })
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getState(@PathVariable String orderId) {
        return stateRepo.findByOrderId(orderId)
                .map(state -> ResponseEntity
                        .status(BillingSuccessCode.PAYMENT_STATUS_FETCHED.getStatus())
                        .body(SuccessResponse.from(BillingSuccessCode.PAYMENT_STATUS_FETCHED, PaymentStateResponse.from(state))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private String mask(String v) {
        return (v == null || v.length() < 4) ? "***" : v.substring(0, 2) + "***";
    }
}
