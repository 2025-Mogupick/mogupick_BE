package subscribenlike.mogupick.cart.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;

    @ManyToOne
    private SubscriptionOption option; // 주기옵션

    private LocalDate firstDeliveryDate; // 첫 배송 희망일

    public CartItem(Product product, SubscriptionOption option, LocalDate firstDeliveryDate) {
        this.product = product;
        this.option = option;
        this.firstDeliveryDate = firstDeliveryDate;
    }

    public static CartItem create(Product product, SubscriptionOption option, LocalDate firstDeliveryDate) {
        return new CartItem(product, option, firstDeliveryDate);
    }

    public void assignCart(Cart cart) {
        this.cart = cart;
    }
    public void removeCart() {
        this.cart = null;
    }
    public void updateOption(SubscriptionOption option, LocalDate firstDeliveryDate) {
        this.option = option;
        this.firstDeliveryDate = firstDeliveryDate;
    }
}
