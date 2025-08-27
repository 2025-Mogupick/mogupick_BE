package subscribenlike.mogupick.brand.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subscribenlike.mogupick.brand.dto.BrandCreateRequest;
import subscribenlike.mogupick.brand.dto.BrandResponse;
import subscribenlike.mogupick.brand.service.BrandService;

@RestController
@RequestMapping("/api/v1/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> create(Long memberId, @RequestBody BrandCreateRequest brandCreateRequest) {
        return ResponseEntity.ok(brandService.save(memberId, brandCreateRequest));
    }
}
