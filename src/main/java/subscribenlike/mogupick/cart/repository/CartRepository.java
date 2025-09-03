package subscribenlike.mogupick.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subscribenlike.mogupick.cart.common.exception.CartErrorCode;
import subscribenlike.mogupick.cart.common.exception.CartException;
import subscribenlike.mogupick.cart.domain.Cart;
import subscribenlike.mogupick.member.domain.Member;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMember(Member member);

    default Cart findOrCreate(Member member) {
        return findByMember(member)
                .orElseGet(() -> save(Cart.create(member)));
    }

    default Cart findOrThrow(Member member) {
        return findByMember(member)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));
    }
}
