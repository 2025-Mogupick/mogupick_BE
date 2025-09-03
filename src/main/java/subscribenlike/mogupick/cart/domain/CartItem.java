package subscribenlike.mogupick.cart.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionPeriodUnit;

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

    @Enumerated(EnumType.STRING)
     private SubscriptionPeriodUnit unit;

    private int period;

    private CartItem(Product product, SubscriptionPeriodUnit unit, int period) {
        this.product = product;
        this.unit = unit;
        this.period = period;
    }

    public static CartItem create(Product product, SubscriptionPeriodUnit unit, int period) {
        return new CartItem(product, unit, period);
    }

    public void assignCart(Cart cart) {
        this.cart = cart;
    }

    public void removeCart() {
        this.cart = null;
    }

    public void updateOption(SubscriptionPeriodUnit unit, int period) {
        this.unit = unit;
        this.period = period;
    }
}
