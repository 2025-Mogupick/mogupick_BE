package subscribenlike.mogupick.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;

@Entity
@Table(name = "product_view_count")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductViewCount extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long viewCount;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    public ProductViewCount(Product product) {
        this.product = product;
        this.viewCount = 0L;
    }

    public static ProductViewCount of(Product product) {
        return new ProductViewCount(product);
    }

    public void increase() {
        this.viewCount += 1L;
    }

    public void updateViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}


