package subscribenlike.mogupick.order.domain;

import jakarta.persistence.*;
import lombok.*;
import subscribenlike.mogupick.common.domain.BaseEntity;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.subscriptionOption.domain.SubscriptionOption;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    private Long productId;
    private String productName;
    private String brandName;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private SubscriptionOption option;

    private LocalDate firstDeliveryDate;

    private OrderItem(Long productId, String productName, String brandName, int price,
                      SubscriptionOption option, LocalDate firstDeliveryDate) {
        this.productId = productId;
        this.productName = productName;
        this.brandName = brandName;
        this.price = price;
        this.option = option;
        this.firstDeliveryDate = firstDeliveryDate;
    }

    public static OrderItem from(Product p, SubscriptionOption option, LocalDate firstDeliveryDate) {
        return new OrderItem(p.getId(), p.getName(), p.getBrandName(), p.getPrice(), option, firstDeliveryDate);
    }

    void bind(Order order) {
        this.order = order;
    }
}
