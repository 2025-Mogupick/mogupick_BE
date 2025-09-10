package subscribenlike.mogupick.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import subscribenlike.mogupick.category.domain.RootCategory;
import subscribenlike.mogupick.category.domain.SubCategory;
import subscribenlike.mogupick.common.utils.GlobalLogger;
import subscribenlike.mogupick.product.common.ProductErrorCode;
import subscribenlike.mogupick.product.common.ProductException;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.domain.ProductOption;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends MongoRepository<ProductOption, String> {

    List<ProductOption> findAllByRootCategory(RootCategory rootCategory);

    Optional<ProductOption> findByProductId(Long productId);

    List<ProductOption> findAllBySubCategory(SubCategory subCategory);

    default ProductOption getByProductId(Long productId) {
        return findByProductId(productId).orElseGet(()->{
            GlobalLogger.warn(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());
            return null;
        });
    }
}
