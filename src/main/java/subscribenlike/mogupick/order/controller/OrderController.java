package subscribenlike.mogupick.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.auth.domain.PrincipalDetails;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.order.common.success.OrderSuccessCode;
import subscribenlike.mogupick.order.dto.CreateOrderRequest;
import subscribenlike.mogupick.order.dto.OrderResponse;
import subscribenlike.mogupick.order.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "체크된 장바구니 아이템과 주소로 주문을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "주문 생성 성공")
    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody CreateOrderRequest req
    ) {
        OrderResponse res = orderService.create(user.getId(), req);
        return ResponseEntity
                .status(OrderSuccessCode.ORDER_CREATED.getStatus())
                .body(SuccessResponse.from(OrderSuccessCode.ORDER_CREATED, res));
    }

    @Operation(summary = "주문 조회", description = "orderId로 주문 상세를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주문 조회 성공")
    @GetMapping("/{orderId}")
    public ResponseEntity<?> get(@PathVariable String orderId) {
        OrderResponse res = orderService.get(orderId);
        return ResponseEntity
                .status(OrderSuccessCode.ORDER_FETCHED.getStatus())
                .body(SuccessResponse.from(OrderSuccessCode.ORDER_FETCHED, res));
    }
}
