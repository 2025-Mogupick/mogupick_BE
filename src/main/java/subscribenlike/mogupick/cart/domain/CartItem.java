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

    @Column(nullable = false)
    private int priceSnapshot;

    private CartItem(Product product, SubscriptionOption option, LocalDate firstDeliveryDate, int priceSnapshot) {
        this.product = product;
        this.option = option;
        this.firstDeliveryDate = firstDeliveryDate;
        this.priceSnapshot = priceSnapshot;
    }

    public static CartItem create(Product product, SubscriptionOption option, LocalDate firstDeliveryDate, int priceSnapshot) {
        return new CartItem(product, option, firstDeliveryDate, priceSnapshot);
    }

    public void assignCart(Cart cart) {
        this.cart = cart;
    }
    public void removeCart() {
        this.cart = null;
    }
    public void updateOption(SubscriptionOption option, LocalDate firstDeliveryDate, int priceSnapshot) {
        this.option = option;
        this.firstDeliveryDate = firstDeliveryDate;
        this.priceSnapshot = priceSnapshot;
    }
}
