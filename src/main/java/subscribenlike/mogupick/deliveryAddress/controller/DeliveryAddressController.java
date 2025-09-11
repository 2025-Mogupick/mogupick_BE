package subscribenlike.mogupick.deliveryAddress.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import subscribenlike.mogupick.auth.domain.PrincipalDetails;
import subscribenlike.mogupick.deliveryAddress.common.success.DeliveryAddressSuccessCode;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressRequest;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressResponse;
import subscribenlike.mogupick.deliveryAddress.dto.DeliveryAddressUpdateRequest;
import subscribenlike.mogupick.deliveryAddress.service.DeliveryAddressService;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.global.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery-addresses")
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    @Operation(summary = "배송지 등록", description = "회원의 새로운 배송지를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "배송지 등록 성공")
    })
    @PostMapping
    public ResponseEntity<?> register(@AuthenticationPrincipal PrincipalDetails userDetails,
                                      @RequestBody DeliveryAddressRequest request) {
        Long memberId = userDetails.getId();
        var res = deliveryAddressService.register(memberId, request);
        return ResponseEntity
                .status(DeliveryAddressSuccessCode.ADDRESS_REGISTERED.getStatus())
                .body(SuccessResponse.from(DeliveryAddressSuccessCode.ADDRESS_REGISTERED, res));
    }

    @Operation(summary = "배송지 목록 조회", description = "회원의 모든 배송지 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송지 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<?> getAll(@AuthenticationPrincipal PrincipalDetails userDetails) {
        Long memberId = userDetails.getId();
        List<DeliveryAddressResponse> res = deliveryAddressService.findAllByMemberId(memberId);
        return ResponseEntity
                .status(DeliveryAddressSuccessCode.ADDRESS_LIST_FETCHED.getStatus())
                .body(SuccessResponse.from(DeliveryAddressSuccessCode.ADDRESS_LIST_FETCHED, res));
    }

    @Operation(summary = "배송지 수정", description = "특정 배송지를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배송지 수정 성공"),
    })
    @PutMapping("/{addressId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal PrincipalDetails userDetails,
                                    @PathVariable Long addressId,
                                    @RequestBody DeliveryAddressUpdateRequest request) {
        Long memberId = userDetails.getId();
        var res = deliveryAddressService.update(memberId, addressId, request);
        return ResponseEntity
                .status(DeliveryAddressSuccessCode.ADDRESS_UPDATED.getStatus())
                .body(SuccessResponse.from(DeliveryAddressSuccessCode.ADDRESS_UPDATED, res));
    }

    @Operation(summary = "배송지 삭제", description = "특정 배송지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "배송지 삭제 성공"),
    })
    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal PrincipalDetails userDetails,
                                    @PathVariable Long addressId) {
        Long memberId = userDetails.getId();
        deliveryAddressService.delete(memberId, addressId);
        return ResponseEntity
                .status(DeliveryAddressSuccessCode.ADDRESS_DELETED.getStatus())
                .body(SuccessResponse.from(DeliveryAddressSuccessCode.ADDRESS_DELETED, null));
    }
}
