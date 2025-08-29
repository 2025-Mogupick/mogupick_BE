package subscribenlike.mogupick.product;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQuerydslRepository {
}
