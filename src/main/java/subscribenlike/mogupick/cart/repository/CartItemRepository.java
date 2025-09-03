package subscribenlike.mogupick.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.cart.domain.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
