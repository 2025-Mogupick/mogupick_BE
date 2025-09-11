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
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;
import subscribenlike.mogupick.subscriptionOption.repository.SubscriptionOptionRepository;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionOptionRepository subscriptionOptionRepository;

    @Transactional
    public CartResponse get(Long memberId) {
        Member member = memberRepository.findOrThrow(memberId);
        Cart cart = cartRepository.findOrCreate(member);
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse add(Long memberId, CartAddRequest request) {
        Member member = memberRepository.findOrThrow(memberId);
        Product product = productRepository.getById(request.productId());
        SubscriptionOption option = subscriptionOptionRepository.findById(request.subscriptionOptionId())
                .orElseThrow(() -> new CartException(CartErrorCode.SUBSCRIPTION_OPTION_NOT_FOUND));

        if (request.firstDeliveryDate() == null) {
            throw new CartException(CartErrorCode.FIRST_DELIVERY_DATE_REQUIRED); // 필요시 신규 에러코드 추가
        }

        int price = product.getPrice();

        Cart cart = cartRepository.findOrCreate(member);
        cart.addItem(CartItem.create(product, option, request.firstDeliveryDate(), price));
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
        Member member = memberRepository.findOrThrow(memberId);
        Cart cart = cartRepository.findOrThrow(member);
        CartItem target = cart.getItems().stream()
                .filter(i -> Objects.equals(i.getId(), cartItemId))
                .findFirst()
                .orElseThrow(() -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));

        SubscriptionOption option = subscriptionOptionRepository.findById(req.subscriptionOptionId())
                .orElseThrow(() -> new CartException(CartErrorCode.SUBSCRIPTION_OPTION_NOT_FOUND));

        LocalDate firstDeliveryDate = req.firstDeliveryDate();
        if (firstDeliveryDate == null) {
            throw new CartException(CartErrorCode.FIRST_DELIVERY_DATE_REQUIRED);
        }

        CartItem duplicate = cart.getItems().stream()
                .filter(i -> !Objects.equals(i.getId(), cartItemId))
                .filter(i -> Objects.equals(i.getProduct().getId(), target.getProduct().getId()))
                .filter(i -> i.getOption().getId().equals(option.getId()))
                .filter(i -> i.getFirstDeliveryDate().equals(firstDeliveryDate))
                .findFirst()
                .orElse(null);

        if (duplicate != null) {
            cart.getItems().remove(target);
            target.removeCart();
            return CartResponse.from(cart);
        }

        int price = target.getProduct().getPrice();
        target.updateOption(option, firstDeliveryDate, price);

        return CartResponse.from(cart);
    }
}
