package subscribenlike.mogupick.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.global.security.CustomUserDetails;
import subscribenlike.mogupick.like.service.LikeService;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/product/{productId}")
    public ResponseEntity<Void> updateProductLike(
            @PathVariable("productId") Long productId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        likeService.updateProductLike(productId, user.getMemberId());
        return ResponseEntity.ok().build();
    }
}
