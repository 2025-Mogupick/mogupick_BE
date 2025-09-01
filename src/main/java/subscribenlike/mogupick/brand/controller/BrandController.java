package subscribenlike.mogupick.brand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "브랜드 생성", description = "브랜드를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "브랜드 등록 성공"
            )
    })
    @PostMapping
    public ResponseEntity<BrandResponse> create(Long memberId, @RequestBody BrandCreateRequest brandCreateRequest) {
        return ResponseEntity.ok(brandService.save(memberId, brandCreateRequest));
    }

    @Operation(summary = "브랜드 삭제", description = "브랜드를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "브랜드 삭제 성공"
            )
    })
    @DeleteMapping("{brandId}")
    public ResponseEntity<Void> delete(Long memberId, @PathVariable Long brandId) {
        brandService.delete(memberId,brandId);
        return ResponseEntity.ok().build();
    }
}
