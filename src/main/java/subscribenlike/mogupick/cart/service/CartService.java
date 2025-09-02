package subscribenlike.mogupick.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribenlike.mogupick.cart.common.exception.CartErrorCode;
import subscribenlike.mogupick.cart.common.exception.CartException;
import subscribenlike.mogupick.cart.domain.Cart;
import subscribenlike.mogupick.cart.domain.CartItem;
import subscribenlike.mogupick.cart.dto.CartAddRequest;
import subscribenlike.mogupick.cart.dto.CartItemOptionUpdateRequest;
import subscribenlike.mogupick.cart.dto.CartResponse;
import subscribenlike.mogupick.cart.repository.CartRepository;
import subscribenlike.mogupick.member.domain.Member;
import subscribenlike.mogupick.member.repository.MemberRepository;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.repository.ProductRepository;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;

import java.util.Iterator;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public CartResponse get(Long memberId) {
        Member member = memberRepository.findOrThrow(memberId);
        Cart cart = cartRepository.findOrCreate(member);
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse add(CartAddRequest request) {
        Member member = memberRepository.findOrThrow(request.memberId());
        Product product = productRepository.getById(request.productId());

        if (request.period() <= 0) {
            throw new CartException(CartErrorCode.PERIOD_INVALID);
        }

        SubscriptionPeriodUnit unit = request.unit();
        if (unit == null) {
            throw new CartException(CartErrorCode.UNIT_INVALID);
        }

        Cart cart = cartRepository.findOrCreate(member);
        cart.addItem(CartItem.create(product, unit, request.period()));

        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse removeItem(Long memberId, Long cartItemId) {
        Member member = memberRepository.findOrThrow(memberId);
        Cart cart = cartRepository.findOrThrow(member);

        boolean removed = false;
        for (Iterator<CartItem> it = cart.getItems().iterator(); it.hasNext(); ) {
            CartItem item = it.next();
            if (item.getId().equals(cartItemId)) {
                it.remove();
                item.removeCart();
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new CartException(CartErrorCode.CART_ITEM_NOT_FOUND);
        }
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse updateItemOption(Long memberId, Long cartItemId, CartItemOptionUpdateRequest req) {
        if (req.period() <= 0) {
            throw new CartException(CartErrorCode.PERIOD_INVALID);
        }
        if (req.unit() == null) {
            throw new CartException(CartErrorCode.UNIT_INVALID);
        }

        Member member = memberRepository.findOrThrow(memberId);
        Cart cart = cartRepository.findOrThrow(member);

        CartItem target = cart.getItems().stream()
                .filter(i -> Objects.equals(i.getId(), cartItemId))
                .findFirst()
                .orElseThrow(() -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));

        CartItem duplicate = cart.getItems().stream()
                .filter(i -> !Objects.equals(i.getId(), cartItemId))
                .filter(i -> Objects.equals(i.getProduct().getId(), target.getProduct().getId()))
                .filter(i -> i.getUnit() == req.unit() && i.getPeriod() == req.period())
                .findFirst()
                .orElse(null);

        if (duplicate != null) {
            cart.getItems().remove(target);
            target.removeCart();
            return CartResponse.from(cart);
        }

        setOption(target, req.unit(), req.period());

        return CartResponse.from(cart);
    }

    private void setOption(CartItem item, SubscriptionPeriodUnit unit, int period) {
        item.updateOption(unit, period);
    }
}
