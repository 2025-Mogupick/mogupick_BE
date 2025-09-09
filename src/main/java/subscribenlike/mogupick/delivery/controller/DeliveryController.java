package subscribenlike.mogupick.delivery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.delivery.common.success.DeliverySuccessCode;
import subscribenlike.mogupick.delivery.service.DeliveryService;

@Tag(name = "Delivery (관리자용)", description = "배송 관리 API")
@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "배송 시작 처리", description = "특정 배송 건의 상태를 '배송 시작'으로 변경하고 알림을 보냅니다.")
    @PostMapping("/{deliveryId}/start")
    public SuccessResponse<Void> startDelivery(@PathVariable Long deliveryId) {
        deliveryService.startDelivery(deliveryId);
        return SuccessResponse.from(DeliverySuccessCode.DELIVERY_STARTED_SUCCESS);
    }

    @Operation(summary = "배송 완료 처리", description = "특정 배송 건의 상태를 '배송 완료'로 변경하고 알림을 보냅니다.")
    @PostMapping("/{deliveryId}/complete")
    public SuccessResponse<Void> completeDelivery(@PathVariable Long deliveryId) {
        deliveryService.completeDelivery(deliveryId);
        return SuccessResponse.from(DeliverySuccessCode.DELIVERY_COMPLETED_SUCCESS);
    }
}