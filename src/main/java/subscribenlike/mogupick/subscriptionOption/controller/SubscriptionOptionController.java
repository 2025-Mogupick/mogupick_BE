package subscribenlike.mogupick.subscriptionOption.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.cart.common.success.CartSuccessCode;
import subscribenlike.mogupick.common.success.SuccessResponse;
import subscribenlike.mogupick.subscriptionOption.common.SubscriptionOptionSuccessCode;
import subscribenlike.mogupick.subscriptionOption.dto.SubscriptionOptionResponse;
import subscribenlike.mogupick.subscriptionOption.service.SubscriptionOptionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscription-options")
public class SubscriptionOptionController {
    private final SubscriptionOptionService subscriptionOptionService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<SubscriptionOptionResponse> response = subscriptionOptionService.getAll();
        return ResponseEntity
                .status(SubscriptionOptionSuccessCode.OPTION_FETCHED.getStatus())
                .body(SuccessResponse.from(SubscriptionOptionSuccessCode.OPTION_FETCHED, response));
    }
}
