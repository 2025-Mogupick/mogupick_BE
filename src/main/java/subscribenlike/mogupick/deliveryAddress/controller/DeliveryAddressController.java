package subscribenlike.mogupick.deliveryAddress.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public ResponseEntity<?> register(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @RequestBody DeliveryAddressRequest request) {
        Long memberId = userDetails.getMemberId();
        var res = deliveryAddressService.register(memberId, request);
        return ResponseEntity
                .status(DeliveryAddressSuccessCode.ADDRESS_REGISTERED.getStatus())
                .body(SuccessResponse.from(DeliveryAddressSuccessCode.ADDRESS_REGISTERED, res));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        List<DeliveryAddressResponse> res = deliveryAddressService.findAllByMemberId(memberId);
        return ResponseEntity
                .status(DeliveryAddressSuccessCode.ADDRESS_LIST_FETCHED.getStatus())
                .body(SuccessResponse.from(DeliveryAddressSuccessCode.ADDRESS_LIST_FETCHED, res));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long addressId,
                                    @RequestBody DeliveryAddressUpdateRequest request) {
        Long memberId = userDetails.getMemberId();
        var res = deliveryAddressService.update(memberId, addressId, request);
        return ResponseEntity
                .status(DeliveryAddressSuccessCode.ADDRESS_UPDATED.getStatus())
                .body(SuccessResponse.from(DeliveryAddressSuccessCode.ADDRESS_UPDATED, res));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long addressId) {
        Long memberId = userDetails.getMemberId();
        deliveryAddressService.delete(memberId, addressId);
        return ResponseEntity
                .status(DeliveryAddressSuccessCode.ADDRESS_DELETED.getStatus())
                .body(SuccessResponse.from(DeliveryAddressSuccessCode.ADDRESS_DELETED, null));
    }
}
