package subscribenlike.mogupick.brand.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.brand.common.BrandErrorCode;
import subscribenlike.mogupick.brand.common.BrandException;
import subscribenlike.mogupick.brand.domain.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    default Brand findOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new BrandException(BrandErrorCode.BRAND_NOT_FOUND));
    }
}
