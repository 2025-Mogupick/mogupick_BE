package subscribenlike.mogupick.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.cart.common.success.CartSuccessCode;
import subscribenlike.mogupick.cart.dto.CartAddRequest;
import subscribenlike.mogupick.cart.dto.CartItemOptionUpdateRequest;
import subscribenlike.mogupick.cart.dto.CartResponse;
import subscribenlike.mogupick.cart.service.CartService;
import subscribenlike.mogupick.common.success.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 조회", description = "회원의 장바구니를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 조회 성공")
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getCart(@PathVariable Long memberId) {
        CartResponse response = cartService.get(memberId);
        return ResponseEntity
                .status(CartSuccessCode.CART_FETCHED.getStatus())
                .body(SuccessResponse.from(CartSuccessCode.CART_FETCHED, response));
    }

    @Operation(summary = "장바구니 담기", description = "상품을 장바구니에 담습니다. (구독 주기 포함)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 담기 성공")
    })
    @PostMapping
    public ResponseEntity<?> add(@RequestBody CartAddRequest request) {
        CartResponse response = cartService.add(request);
        return ResponseEntity
                .status(CartSuccessCode.CART_ITEM_ADDED.getStatus())
                .body(SuccessResponse.from(CartSuccessCode.CART_ITEM_ADDED, response));
    }

    @Operation(summary = "장바구니 아이템 옵션 변경", description = "장바구니 아이템의 구독 옵션(단위/주기)을 변경합니다. 중복 라인이 있으면 병합됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "옵션 변경 성공")
    })
    @PatchMapping("/{memberId}/items/{cartItemId}/option")
    public ResponseEntity<?> updateItemOption(@PathVariable Long memberId,
                                              @PathVariable Long cartItemId,
                                              @RequestBody CartItemOptionUpdateRequest request) {
        CartResponse response = cartService.updateItemOption(memberId, cartItemId, request);
        return ResponseEntity
                .status(CartSuccessCode.CART_ITEM_OPTION_UPDATED.getStatus())
                .body(SuccessResponse.from(CartSuccessCode.CART_ITEM_OPTION_UPDATED, response));
    }

    @Operation(summary = "장바구니 아이템 삭제", description = "장바구니에서 특정 아이템을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 삭제 성공")
    })
    @DeleteMapping("/{memberId}/items/{cartItemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long memberId,
                                        @PathVariable Long cartItemId) {
        CartResponse response = cartService.removeItem(memberId, cartItemId);
        return ResponseEntity
                .status(CartSuccessCode.CART_ITEM_REMOVED.getStatus())
                .body(SuccessResponse.from(CartSuccessCode.CART_ITEM_REMOVED, response));
    }
}
