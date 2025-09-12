package subscribenlike.mogupick.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;
import subscribenlike.mogupick.member.domain.Member;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderId;

    @ManyToOne
    private Member member;

    @Embedded
    private AddressSnapshot addressSnapshot;

    private int payableAmount;

    private String orderName;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> items = new LinkedHashSet<>();

    private Order(String orderId, Member member, AddressSnapshot addr, int amount, String orderName) {
        this.orderId = orderId;
        this.member = member;
        this.addressSnapshot = addr;
        this.payableAmount = amount;
        this.orderName = orderName;
        this.status = OrderStatus.CREATED;
    }
    public static Order create(String orderId, Member member, AddressSnapshot addr, int amount, String orderName) {
        return new Order(orderId, member, addr, amount, orderName);
    }

    public void addItem(OrderItem item) {
        items.add(item); item.bind(this);
    }

    public void markPaid() {
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELED;
    }
}
