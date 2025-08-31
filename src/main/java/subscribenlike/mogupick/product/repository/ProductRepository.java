package subscribenlike.mogupick.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.product.common.ProductErrorCode;
import subscribenlike.mogupick.product.common.ProductException;
import subscribenlike.mogupick.product.domain.Product;

import java.util.List;
import java.util.Map;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQuerydslRepository {

    List<Product> findAllByIdIn(List<Long> productIds);

    default Map<Long, Product> findAllByIdInMaps(List<Long> productIds) {
        return findAllByIdIn(productIds).stream()
                .collect(java.util.stream.Collectors.toMap(Product::getId, product -> product));
    }

    default Product getById(Long productId) {
        return findById(productId).orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }
}
