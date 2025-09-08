package subscribenlike.mogupick.subscription.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.subscription.common.success.SubscriptionSuccessCode;
import subscribenlike.mogupick.subscription.domain.SubscriptionStatus;
import subscribenlike.mogupick.subscription.dto.*;
import subscribenlike.mogupick.subscription.service.SubscriptionService;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Operation(summary = "구독 리스트", description = "회원의 구독 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "구독 리스트 조회 성공")
    @GetMapping
    public ResponseEntity<?> getList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) SubscriptionStatus status
    ) {
        Long memberId = userDetails.getMemberId();
        List<SubscriptionResponse> response = subscriptionService.getList(memberId, status);
        return ResponseEntity
                .status(SubscriptionSuccessCode.SUBSCRIPTION_LIST_FETCHED.getStatus())
                .body(SuccessResponse.from(SubscriptionSuccessCode.SUBSCRIPTION_LIST_FETCHED, response));
    }

    @Operation(summary = "구독 상세", description = "구독 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "구독 상세 조회 성공")
    @GetMapping("/{subscriptionId}")
    public ResponseEntity<?> getDetail(@PathVariable Long subscriptionId) {
        SubscriptionResponse response = subscriptionService.getDetail(subscriptionId);
        return ResponseEntity
                .status(SubscriptionSuccessCode.SUBSCRIPTION_DETAIL_FETCHED.getStatus())
                .body(SuccessResponse.from(SubscriptionSuccessCode.SUBSCRIPTION_DETAIL_FETCHED, response));
    }

    @Operation(summary = "구독 캘린더", description = "월별 구독 결제 일정을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "구독 캘린더 조회 성공")
    @GetMapping("/calendar")
    public ResponseEntity<?> getCalendar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String yearMonth // "2025-08"
    ) {
        Long memberId = userDetails.getMemberId();
        SubscriptionCalendarResponse response = subscriptionService.getCalendar(
                memberId, YearMonth.parse(yearMonth)
        );
        return ResponseEntity
                .status(SubscriptionSuccessCode.SUBSCRIPTION_CALENDAR_FETCHED.getStatus())
                .body(SuccessResponse.from(SubscriptionSuccessCode.SUBSCRIPTION_CALENDAR_FETCHED, response));
    }

    @Operation(summary = "구독 해지", description = "구독을 해지합니다.")
    @ApiResponse(responseCode = "200", description = "구독 해지 성공")
    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<?> cancel(@PathVariable Long subscriptionId) {
        SubscriptionResponse response = subscriptionService.cancel(subscriptionId);
        return ResponseEntity
                .status(SubscriptionSuccessCode.SUBSCRIPTION_CANCELLED.getStatus())
                .body(SuccessResponse.from(SubscriptionSuccessCode.SUBSCRIPTION_CANCELLED, response));
    }

    @Operation(summary = "구독 옵션 변경", description = "구독 옵션(주기, 희망 배송일)을 변경합니다.")
    @ApiResponse(responseCode = "200", description = "구독 옵션 변경 성공")
    @PatchMapping("/{subscriptionId}/option")
    public ResponseEntity<?> changeOption(
            @PathVariable Long subscriptionId,
            @RequestBody SubscriptionOptionUpdateRequest request) {
        SubscriptionResponse response = subscriptionService.changeOption(subscriptionId, request);
        return ResponseEntity
                .status(SubscriptionSuccessCode.SUBSCRIPTION_OPTION_UPDATED.getStatus())
                .body(SuccessResponse.from(SubscriptionSuccessCode.SUBSCRIPTION_OPTION_UPDATED, response));
    }
}
