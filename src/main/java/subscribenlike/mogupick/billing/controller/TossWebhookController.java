package subscribenlike.mogupick.billing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.billing.common.success.BillingSuccessCode;
import subscribenlike.mogupick.billing.dto.TossWebhookAckResponse;
import subscribenlike.mogupick.billing.dto.TossWebhookRequest;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.subscription.service.SubscriptionService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhooks/toss")
public class TossWebhookController {

    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "토스 결제 웹훅 수신",
            description = "토스 결제 이벤트 웹훅을 수신하여 구독 생성 등 후처리를 수행합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹훅 처리 성공"),
    })
    @PostMapping("/payment")
    public ResponseEntity<?> handlePaymentWebhook(@Valid @RequestBody TossWebhookRequest webhook) {
        log.info("토스 결제 웹훅 수신: eventType={}", webhook.eventType());

        try {
            if ("Payment.Done".equals(webhook.eventType())) {
                subscriptionService.createFromPayment(
                        webhook.data().paymentKey(),
                        webhook.data().orderId()
                );
                log.info("구독 생성 처리 완료: paymentKey={}", webhook.data().paymentKey());

                TossWebhookAckResponse body = TossWebhookAckResponse.processed(
                        webhook.eventType(),
                        mask(webhook.data().paymentKey()),
                        webhook.data().orderId()
                );

                return ResponseEntity
                        .status(BillingSuccessCode.TOSS_WEBHOOK_HANDLED.getStatus())
                        .body(SuccessResponse.from(BillingSuccessCode.TOSS_WEBHOOK_HANDLED, body));
            }

            log.info("처리 대상 아님(ACK만 반환): eventType={}", webhook.eventType());
            TossWebhookAckResponse body = TossWebhookAckResponse.ignored(webhook.eventType());

            return ResponseEntity
                    .status(BillingSuccessCode.TOSS_WEBHOOK_HANDLED.getStatus())
                    .body(SuccessResponse.from(BillingSuccessCode.TOSS_WEBHOOK_HANDLED, body));

        } catch (Exception e) {
            log.error("웹훅 처리 실패", e);
            TossWebhookAckResponse body = TossWebhookAckResponse.ignored(webhook.eventType());
            return ResponseEntity
                    .status(BillingSuccessCode.TOSS_WEBHOOK_HANDLED.getStatus())
                    .body(SuccessResponse.from(BillingSuccessCode.TOSS_WEBHOOK_HANDLED, body));
        }
    }

    private String mask(String v) {
        return (v == null || v.length() < 6) ? "***" : v.substring(0, 3) + "***";
    }
}
