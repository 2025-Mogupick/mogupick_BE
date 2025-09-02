package subscribenlike.mogupick.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.product.common.ProductErrorCode;
import subscribenlike.mogupick.product.common.ProductException;
import subscribenlike.mogupick.product.domain.ProductOption;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends MongoRepository<ProductOption, String> {

    List<ProductOption> findAllByRootCategory(RootCategory rootCategory);
    Optional<ProductOption> findByProductId(Long productId);

    default ProductOption getByProductId(Long productId) {
        return findByProductId(productId).
                orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }
}
