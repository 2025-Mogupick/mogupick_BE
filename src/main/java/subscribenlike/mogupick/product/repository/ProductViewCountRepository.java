package subscribenlike.mogupick.product.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.product.common.ProductErrorCode;
import subscribenlike.mogupick.product.common.ProductException;
import subscribenlike.mogupick.product.domain.ProductViewCount;

public interface ProductViewCountRepository extends JpaRepository<ProductViewCount, Long> {

    Optional<ProductViewCount> findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    default ProductViewCount getByProductId(Long productId) {
        return findByProductId(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }
}


