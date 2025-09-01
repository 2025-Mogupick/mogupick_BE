package subscribenlike.mogupick.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQuerydslRepository {
    List<Product> findByNameContainingIgnoreCase(String keyword);
}
