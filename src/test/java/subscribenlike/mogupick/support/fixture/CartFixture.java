package subscribenlike.mogupick.support.fixture;

import subscribenlike.mogupick.cart.domain.Cart;
import subscribenlike.mogupick.cart.domain.CartItem;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;

import java.time.LocalDate;

public enum CartFixture {
    ;

    public static Cart 빈카트(subscribenlike.mogupick.member.domain.Member member) {
        return Cart.create(member);
    }

    public static Cart 상품하나담은카트(subscribenlike.mogupick.member.domain.Member member,
                                Product product,
                                SubscriptionOption option,
                                LocalDate firstDeliveryDate) {
        Cart cart = Cart.create(member);
        cart.addItem(CartItem.create(product, option, firstDeliveryDate));
        return cart;
    }

    public static Cart 두아이템담은카트(subscribenlike.mogupick.member.domain.Member member,
                                Product product,
                                SubscriptionOption option1,
                                LocalDate firstDeliveryDate1,
                                SubscriptionOption option2,
                                LocalDate firstDeliveryDate2) {
        Cart cart = Cart.create(member);
        cart.addItem(CartItem.create(product, option1, firstDeliveryDate1));
        cart.addItem(CartItem.create(product, option2, firstDeliveryDate2));
        return cart;
    }
}
